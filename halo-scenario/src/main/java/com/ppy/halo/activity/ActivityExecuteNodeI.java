package com.ppy.halo.activity;


import com.ppy.halo.request.Request;
import com.ppy.halo.response.Response;

import java.util.function.Function;

/**
 * 活动节点接口类，定义执行的标准，如下：
 * 1）通过{@link ActivityExecuteNodeI#in(Request, Response)} 接口将request/response对象转换为 DomainInput 对象
 * 2）通过{@link ActivityExecuteNodeI#handle(DomainInput)} 接口以DomainInput对象为参数，调用领域服务对象并返回领域输出对象
 * 3）通过{@link ActivityExecuteNodeI#out(DomainOutput,Response)} 接口将领域输出对象转换为response 对象
 * @author: jackie
 * @since: 1.0 2023/12/23
 */
public interface ActivityExecuteNodeI<RequestInput,Resp extends Response, DomainInput,DomainOutput,Clazz> {

    /**
     * 将request/response对象转换为 DomainInput 对象
     * @param: [req, resp]
     * @return: DomainInput
     **/
    public DomainInput in(Request<RequestInput> req, Resp resp);
    /**
     * 以DomainInput对象为参数，调用领域服务对象并返回领域输出对象
     * @param: [domainInput]
     * @return: java.util.function.Function<Clazz,DomainOutput>
     **/
    public Function<Clazz,DomainOutput> handle(DomainInput domainInput);
    /**
     * 将领域输出对象转换为response 对象
     * @param: [domainOutput]
     * @return: Resp
     **/
    public Resp out(DomainOutput domainOutput,Resp resp);

    /**
     * @description:根据上下文判断当前的节点是否需要执行
     * @param: [req, resp]
     * @return: java.lang.Boolean
     **/
    default Boolean accept(Request<RequestInput> req, Resp resp){
        return true;
    }
}
