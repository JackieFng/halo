package com.ppy.halo.exception;

/**
 * 提供自定义异常所需的方法
 */
public interface BaseErrorInfoInterface {

    /**
     * 错误码
     * @return
     */
    int getCode();

    /**
     * 错误信息
     * @return
     */
    String getMessage();

}
