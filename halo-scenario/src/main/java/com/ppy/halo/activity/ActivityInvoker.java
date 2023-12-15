package com.ppy.halo.activity;

import cn.hutool.core.lang.Pair;
import com.ppy.halo.domain.DomainFactory;
import com.ppy.halo.exception.DomainBizException;
import com.ppy.halo.exception.InfrastructureException;
import com.ppy.halo.exception.ScenarioException;
import com.ppy.halo.request.Request;
import com.ppy.halo.response.MultiResponse;
import com.ppy.halo.response.Response;
import com.ppy.halo.response.SingleResponse;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Function;

import static com.ppy.halo.exception.ErrorMessage.*;


/**
 * 场景化接口任务节点执行编排器。通过静态方法{@link ActivityInvoker#start(Request, Response)}
 * 传入初始的{@link Request}, {@link Response}对活动进行初始化，再通过对象方法next创建可执行的节点任务，
 * 最后通过invoke 方法触发执行，并得到返回结果。需要注意的地方如下：
 *  1）在每个领域对象执行结束后，需要将结果封装到返回结果 {@link Response}对象中去，该对象为执行链路中的
 *     全局对象，每次做返回结果处理的时候，原则上只需要增量放入本次执行结果，不建议覆盖，否则下个执行节点的数据就丢失了。
 *  2）在start 处做初始传入对象的时候，针对返回对象可以有：{@link Response}、{@link SingleResponse}、{@link MultiResponse}
 *    ，后面两个返回对象还可以传入具体的数据类型。invoke 步骤执行之后，为获得正确结果需要根据传参调用相应的
 *    {@link ActivityInvoker#singleResponse(Class)} {@link ActivityInvoker#multiResponse(Class)}
 *    方法，并且指定正确的数据类型。否则会导致类型转换错误。
 *   一个场景任务编排的正确操作顺序为：
 *   ActivityInvoker.start(res,resp).next(taskA,domainA).next(taskB,domainB).invoke().singleResponse()
 *
 * @author: jackie
 * @version: 1.0
 * @since: 1.0 2022/11/21
 */

public  class ActivityInvoker {

    @Setter
    private Activity activity ;

    private ActivityInvoker(){};

    private static ActivityInvoker createInstance(){
        return new ActivityInvoker();
    }

    /**
     * 开启业务活动，内部创建一个活动对象，并将全局参数保存起来
     * @author: jackie
     * @date: 2022/11/23 11:51
     * @param: [request, response]
     * @return: com.yuansuan.csp.scenario.activity.ActivityInvoker
     **/
    public static <T,Resp extends Response> ActivityInvoker start(Request<T> request, Resp response){
        ActivityInvoker instance = createInstance();
        Activity activity = new Activity(request,response);
        instance.setActivity(activity);
        return instance ;
    }

    /**
     * 传入具体的作业内容，创建一个可执行节点保存到活动对象里面
     * @author: jackie
     * @date: 2022/11/23 11:53
     * @param: [node, serviceType]
     * @return: com.yuansuan.csp.scenario.activity.ActivityInvoker
     **/
    public <RequestInput,Resp extends Response,DomainInput,DomainOutput,ServiceClazz> ActivityInvoker next(ActivityExecuteNodeI<RequestInput,Resp,DomainInput,DomainOutput,ServiceClazz> node, Class<ServiceClazz> serviceType){
        if(this.activity == null ){
            throw new ScenarioException(SCENARIO_ACTIVITY_DEFINE_ERROR.getCode(),SCENARIO_ACTIVITY_DEFINE_ERROR.getMessage());
        }
        this.activity.addActivityNode(node,serviceType);
        return this;
    }

    /**
     * 将活动对象里面的作业遍历执行，如果报错直接抛出异常
     * @author: jackie
     * @date: 2022/11/23 11:54
     * @param: []
     * @return: com.yuansuan.csp.scenario.activity.ActivityInvoker
     **/
    public ActivityInvoker invoke(){
        Activity activity = this.activity;
        if(this.activity == null ){
            throw new ScenarioException(SCENARIO_ACTIVITY_DEFINE_ERROR.getCode(),SCENARIO_ACTIVITY_DEFINE_ERROR.getMessage());
        }
        if(CollectionUtils.isEmpty(activity.getActivityNodeList())){
            return this;
        }
        List<Pair<Class, ActivityExecuteNodeI>> nodeList =  this.activity.getActivityNodeList();
        Response response = null;
        for(Pair<Class, ActivityExecuteNodeI> pair:nodeList){
            Class serviceType = pair.getKey();
            ActivityExecuteNodeI node = pair.getValue();
            Object serviceBean = null;
            if(!node.accept(this.activity.getRequest(),this.activity.getResponse())){
                continue;
            }
            try{
                Object in = node.in(this.activity.getRequest(),this.activity.getResponse());
                serviceBean = DomainFactory.create(serviceType);
                Function func = node.handle(in);
                Object out = null;
                if(func!=null){
                    out = func.apply(serviceBean);
                }
                if(out!=null){
                    response = node.out(out,this.activity.getResponse());
                }
            }catch (InfrastructureException ex) {
                throw new ScenarioException(ex.getErrCode(), ex.getMessage(), ex);
            }catch (DomainBizException ex){
                throw new ScenarioException(ex.getErrCode(),ex.getMessage(),ex);
            }catch (Exception ex){
                throw new ScenarioException(UNKNOWN_ERROR.getMessage(),ex);
            }
            if(!response.isSuccess()){
                throw new ScenarioException(DOMAIN_PROCESS_ERROR.getCode(),String.format(DOMAIN_PROCESS_ERROR.getMessage(),serviceBean.getClass().getSimpleName(),response.getErrCode(),response.getErrMessage()));
            }
            this.activity.setResponse(response);
        }
        return this;
    }

    /**ø
     * 调用执行完毕后，按单个对象的方式返回
     * @param: [r]
     * @return: com.yuansuan.csp.scenario.respone.SingleResponse<R>
     **/
    public <R> SingleResponse<R> singleResponse(Class<R> r){
        return (SingleResponse<R>)this.activity.getResponse();
    }

    /**
     * 调用执行完毕后，按对象集合的方式返回
     * @param: [r]
     * @return: com.yuansuan.csp.scenario.respone.MultiResponse<R>
     **/
    public <R> MultiResponse<R> multiResponse(Class<R> r){
        return (MultiResponse<R>)this.activity.getResponse();
    }

}
