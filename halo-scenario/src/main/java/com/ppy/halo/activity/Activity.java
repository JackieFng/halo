package com.ppy.halo.activity;

import cn.hutool.core.lang.Pair;
import com.ppy.halo.request.Request;
import com.ppy.halo.response.Response;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: jackie
 * @version: 1.0
 * @since: 1.0 2022/11/21
 */
@Data
public  class Activity<T,Resp extends Response>{

    private Request<T> request;

    private Resp response;

    private List<Pair<Class, ActivityExecuteNodeI>> activityNodeList;

    public void addActivityNode(ActivityExecuteNodeI node, Class serviceType){
        if(activityNodeList == null){
            activityNodeList = new ArrayList<>();
        }

        activityNodeList.add(new Pair<>(serviceType,node));
    }

    public Activity(Request<T> request,Resp response){
        this.request = request;
        this.response = response;
    }
}
