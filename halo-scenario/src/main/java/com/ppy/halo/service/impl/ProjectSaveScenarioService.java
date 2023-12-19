package com.ppy.halo.service.impl;

import com.ppy.halo.activity.ActivityExecuteNodeI;
import com.ppy.halo.activity.ActivityInvoker;
import com.ppy.halo.parameter.ProjectSaveParameter;
import com.ppy.halo.parameter.ProjectSaveResponse;
import com.ppy.halo.project.ProjectDomainServiceI;
import com.ppy.halo.project.parameter.ProjectDomainInputDO;
import com.ppy.halo.project.parameter.ProjectDomainOutputDO;
import com.ppy.halo.request.Request;
import com.ppy.halo.response.SingleResponse;
import com.ppy.halo.service.ScenarioServiceI;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * @author: jackie
 * @date: 2023/12/19 15:42
 **/
@Service
public class ProjectSaveScenarioService implements ScenarioServiceI<Request<ProjectSaveParameter>, SingleResponse<ProjectSaveResponse>> {
    @Override
    public SingleResponse<ProjectSaveResponse> startActivity(Request<ProjectSaveParameter> projectSaveParameterRequest) {
        SingleResponse<ProjectSaveResponse> response = new SingleResponse<>();
        response.setData(new ProjectSaveResponse());

        ActivityInvoker.start(projectSaveParameterRequest, response).next(new ActivityExecuteNodeI<ProjectSaveParameter, SingleResponse<ProjectSaveResponse>, ProjectDomainInputDO, ProjectDomainOutputDO, ProjectDomainServiceI>() {

            @Override
            public Boolean accept(Request<ProjectSaveParameter> req, SingleResponse<ProjectSaveResponse> projectSaveResponseSingleResponse) {
                return ActivityExecuteNodeI.super.accept(req, projectSaveResponseSingleResponse);
            }
            @Override
            public ProjectDomainInputDO in(Request<ProjectSaveParameter> req, SingleResponse<ProjectSaveResponse> projectSaveResponseSingleResponse) {
                return null;
            }

            @Override
            public Function<ProjectDomainServiceI, ProjectDomainOutputDO> handle(ProjectDomainInputDO inputDO) {
                return (service) -> service.saveOrUpdateProject(inputDO);
            }

            @Override
            public SingleResponse<ProjectSaveResponse> out(ProjectDomainOutputDO projectDomainOutputDO, SingleResponse<ProjectSaveResponse> projectSaveResponseSingleResponse) {
                return null;
            }
        }, ProjectDomainServiceI.class).invoke().singleResponse(ProjectSaveResponse.class);
        return response;
    }
}
