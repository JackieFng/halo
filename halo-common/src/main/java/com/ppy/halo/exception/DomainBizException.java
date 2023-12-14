package com.ppy.halo.exception;

/**
 * domain 层异常处理封装类
 *
 * @since 1.0.0 2023/12/18
 */
public class DomainBizException extends RuntimeException {

    private String errCode;

    public DomainBizException(String errMessage) {
        super(errMessage);
    }

    public DomainBizException(String errCode, String errMessage) {
        super(errMessage);
        this.errCode = errCode;
    }
    public DomainBizException(String errMessage, Throwable e) {
        super(errMessage, e);
    }

    public DomainBizException(String errCode, String errMessage, Throwable e) {
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
