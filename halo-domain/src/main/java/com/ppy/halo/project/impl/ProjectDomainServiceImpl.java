package com.ppy.halo.project.impl;

import com.ppy.halo.attachment.impl.ProjectAttachment;
import com.ppy.halo.bo.scenario.BizScenario;
import com.ppy.halo.consts.ProductCode;
import com.ppy.halo.domain.Ability;
import com.ppy.halo.domain.Domain;
import com.ppy.halo.extension.authority.AuthorityPermissionExtensionI;
import com.ppy.halo.project.ProjectDomainServiceI;
import com.ppy.halo.project.parameter.ProjectDomainInputDO;
import com.ppy.halo.project.parameter.ProjectDomainOutputDO;
import com.ppy.halo.project.result.ProjectAttachResult;
import com.ppy.halo.register.ExtensionExecutor;
import com.ppy.halo.repository.ProjectRepositoryI;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: jackie
 * @date: 2023/12/19 14:51
 **/
@Service
@Domain(code = "projectDomainService")
public class ProjectDomainServiceImpl implements ProjectDomainServiceI {

    @Resource
    private ProjectAttachment projectAttachment;
    @Resource
    private ProjectRepositoryI projectRepositoryI;
    @Resource
    private ExtensionExecutor extensionExecutor;

    @Override
    public ProjectDomainOutputDO saveOrUpdateProject(ProjectDomainInputDO inputDO) {
        ProjectDomainOutputDO outputDO = new ProjectDomainOutputDO();
        ProjectAttachResult attachResult = projectAttachment.attach(inputDO);
        projectRepositoryI.saveOrUpdateProject(attachResult);
        return outputDO;
    }

    @Override
    @Ability(code = "authorizePermission", desc = "对用户进行授权")
    public Boolean authorizePermissionOnSaveProject() {
        extensionExecutor.execute(AuthorityPermissionExtensionI.class, BizScenario.valueOf(ProductCode.STANDARD__PRODUCT), ext -> ext.getLoadCase2SePermission());
        return Boolean.TRUE;
    }
}


