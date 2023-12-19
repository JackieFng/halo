package com.ppy.halo.repository;

import com.ppy.halo.domain.ProjectDO;
import com.ppy.halo.entity.Project;
import com.ppy.halo.mapper.ProjectMapper;
import com.ppy.halo.project.result.ProjectAttachResult;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author: jackie
 * @date: 2023/12/19 15:32
 **/
@Repository
public class ProjectRepository implements ProjectRepositoryI{

    @Resource
    private ProjectMapper projectMapper;
    @Override
    public Boolean saveOrUpdateProject(ProjectAttachResult result) {
        ProjectDO insertProject = result.getInsertProject();
        if (Objects.nonNull(insertProject)) {
            projectMapper.insert(buildDO2Entity(insertProject));
        }
        ProjectDO updateProject = result.getUpdateProject();
        if (Objects.nonNull(updateProject)) {
            projectMapper.updateById(buildDO2Entity(updateProject));
        }
        return Boolean.TRUE;
    }

    /**
     * 将DO转entity
     *
     * @param projectDO
     * @return
     */
    private Project buildDO2Entity(ProjectDO projectDO){
        Project project = new Project();
        //待转化
        return project;
    }
}
