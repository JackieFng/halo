package com.ppy.halo.exception;

/**
 * 基础设施层异常处理封装类
 *
 * @author jackie
 
 * @since 1.0.0 2022/11/18
 */
public class InfrastructureException extends RuntimeException {

    private String errCode;

    public InfrastructureException(String errMessage) {
        super(errMessage);
    }

    public InfrastructureException(String errCode, String errMessage) {
        super(errMessage);
        this.errCode = errCode;
    }

    public InfrastructureException(String errMessage, Throwable e) {
        super(errMessage, e);
    }

    public InfrastructureException(String errCode, String errMessage, Throwable e) {
        super(errMessage, e);
        this.errCode = errCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

}
