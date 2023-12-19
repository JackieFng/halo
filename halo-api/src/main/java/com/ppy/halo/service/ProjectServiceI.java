package com.ppy.halo.service;

import com.ppy.halo.dto.SaveProjectDTO;

/**
 * @author: jackie
 * @date: 2023/12/19 15:57
 **/
public interface ProjectServiceI {

    void saveOrUpdateProject(SaveProjectDTO dto);
}
