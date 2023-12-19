package com.ppy.halo.repository;

import com.ppy.halo.project.result.ProjectAttachResult;

/**
 * @author: jackie
 * @date: 2023/12/19 15:21
 **/
public interface ProjectRepositoryI {

    /**
     * 保存/更新项目
     *
     * @param result
     * @return
     */
    Boolean saveOrUpdateProject(ProjectAttachResult result);
}
