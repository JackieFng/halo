package com.ppy.halo;

import com.ppy.halo.dto.SaveProjectDTO;
import com.ppy.halo.parameter.ProjectSaveParameter;
import com.ppy.halo.request.Request;
import com.ppy.halo.service.ProjectServiceI;
import com.ppy.halo.service.impl.ProjectSaveScenarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: jackie
 * @date: 2023/12/19 15:57
 **/
@Slf4j
@Service
public class ProjectServiceImpl implements ProjectServiceI {

    @Resource
    private ProjectSaveScenarioService projectSaveScenarioService;

    @Override
    public void saveOrUpdateProject(SaveProjectDTO dto) {
        try {
            Request<ProjectSaveParameter> request = new Request<>();
            ProjectSaveParameter parameter = new ProjectSaveParameter();
            //dto转param
            request.setInputParameter(parameter);
            projectSaveScenarioService.startActivity(request);
        } catch (Exception e) {
            log.error("保存项目异常,{}", e.getMessage());
        }
    }
}
