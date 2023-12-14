package com.ppy.halo.utils.tree;


import com.ppy.halo.utils.tree.bo.TreeBO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: jackie
 * @date: 2023/12/19 11:30
 **/
public class TreeUtil {

    private static final String ROOT_NODE_ID = "0";


    /**
     * 构建树结构
     *
     * @param nodes
     * @param <T>
     * @return
     */
    public static <T> List<? extends TreeBO<?>> buildTree(List<? extends TreeBO<T>> nodes) {
        if (nodes == null || nodes.size() == 0) {
            return null;
        }

        List<TreeBO<T>> topNodes = new ArrayList<>();
        nodes.forEach(node -> {
            //获取父节点id,即把当前节点作为子节点
            Long pid = node.getPid();
            //定位根节点
            if (ROOT_NODE_ID.equals(pid)) {
                topNodes.add(node);
                return;
            }

            boolean hasParent = false;
            for (TreeBO<T> n : nodes) {
                Long id = n.getId();
                //遍历所有节点如果有节点id为该pid即表明node为n的子节点，应为其初始化children并将node添加到children中
                if (id != null && id.equals(pid)) {
                    if (n.getChildren() == null) {
                        n.initChildren();
                    }
                    n.getChildren().add(node);
                    hasParent = true;
                }
            }
            //如果遍历nodes找不到对应的父节点，表明该节点为当前树的根节点
            if (!hasParent) {
                topNodes.add(node);
            }
        });
        return topNodes;
    }
}
