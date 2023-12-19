package com.ppy.halo.project;

import com.ppy.halo.project.parameter.ProjectDomainInputDO;
import com.ppy.halo.project.parameter.ProjectDomainOutputDO;

/**
 * 项目域
 * @author: jackie
 * @date: 2023/12/19 14:36
 **/
public interface ProjectDomainServiceI {
    ProjectDomainOutputDO saveOrUpdateProject(ProjectDomainInputDO inputDO);
}
