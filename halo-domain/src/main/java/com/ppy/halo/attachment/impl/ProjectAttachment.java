package com.ppy.halo.attachment.impl;

import com.ppy.halo.attachment.AttachmentI;
import com.ppy.halo.project.parameter.ProjectDomainInputDO;
import com.ppy.halo.project.result.ProjectAttachResult;
import lombok.Data;

/**
 * @author: jackie
 * @date: 2023/12/19 15:19
 **/
@Data
public class ProjectAttachment implements AttachmentI<ProjectAttachResult, ProjectDomainInputDO> {
    @Override
    public ProjectAttachResult attach(ProjectDomainInputDO inputDO) {
        ProjectAttachResult result = new ProjectAttachResult();
        //做数据转化的处理
        return result;
    }
}
