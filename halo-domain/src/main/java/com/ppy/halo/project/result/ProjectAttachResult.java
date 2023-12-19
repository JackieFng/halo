package com.ppy.halo.project.result;

import com.ppy.halo.domain.ProjectDO;
import lombok.Data;

/**
 * @author: jackie
 * @date: 2023/12/19 15:05
 **/
@Data
public class ProjectAttachResult {

    private ProjectDO insertProject;

    private ProjectDO updateProject;

}
