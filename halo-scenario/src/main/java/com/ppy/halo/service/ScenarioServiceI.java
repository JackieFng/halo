package com.ppy.halo.service;

import com.ppy.halo.request.Request;
import com.ppy.halo.response.Response;
public interface ScenarioServiceI<P extends Request<?>,R extends Response> {
    public R startActivity(P p);
}
