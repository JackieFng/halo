package com.ppy.halo.utils.es.bo;

import lombok.Data;

import java.util.Map;

/**
 * @author: jackie
 * @date: 2023/12/8 14:52
 **/
@Data
public class DocumentBO<T> {
    /**
     * 主键标识，用于es持久化
     */
    private String id;

    /**
     * 文档对象
     */
    private Map data;
}
