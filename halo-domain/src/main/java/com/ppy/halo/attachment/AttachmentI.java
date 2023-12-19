package com.ppy.halo.attachment;

/**
 * 面向存储的数据处理类，专注于将业务数据汇总成存储数据或者某类逻辑数据即将领域对象转成数据库存储对象
 * @author: jackie
 * @date: 2023/12/19 14:34
 **/
public interface AttachmentI<Result, Resp>{
    Result attach(Resp resp);
}
