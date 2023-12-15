package com.ppy.halo.request;

import lombok.Data;

/**
 * scenario层表示返回结果状态的对象封装类
 * @author jackie
 * @since 1.0.0 2023/12/18
 */
@Data
public class Request<T>{
    public T inputParameter;
}
