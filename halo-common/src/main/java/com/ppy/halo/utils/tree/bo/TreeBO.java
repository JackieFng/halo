package com.ppy.halo.utils.tree.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: jackie
 * @date: 2023/12/14 16:16
 **/
@Data
public class TreeBO<T> implements Serializable {
    private Long id;

    private String name;

    private Long pid;

    private List<TreeBO<T>> children;

    public void initChildren() {
        this.children = new ArrayList<>();
    }
}
