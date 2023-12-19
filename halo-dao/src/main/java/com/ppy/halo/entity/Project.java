package com.ppy.halo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
    自定义模板
 * 项目表
 * </p>
 *
 * @author: mybatis-generator
 * @since 2022-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    /**
     * 项目名
     */
    @TableField("project_name")
    private String projectName;

    /**
     * 项目类型如：1-整车项目，2-学科项目，3-竞品项目，4-自研项目
     */
    @TableField("type")
    private Integer type;

    /**
     * 项目基础信息
     */
    @TableField("properties")
    private String properties;

    /**
     * 项目描述
     */
    @TableField("description")
    private String description;

    /**
     * 项目状态 未完成 已完成等
     */
    @TableField("status")
    private Integer status;

    /**
     * 项目是否需要清理tag 0-否1-是
     */
    @TableField("tag")
    private Integer tag;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private String tenantId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField(value = "create_id", fill = FieldFill.INSERT)
    private Long createId;

    /**
     * 修改时间
     */
    @TableField(value ="update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @TableField(value = "update_id", fill = FieldFill.UPDATE)
    private Long updateId;

    /**
     * 删除标识：0-未删除 1-已删除
     */
    @TableField("deleted")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;


}