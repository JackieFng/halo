package com.ppy.halo.exception;

/**
 * 扩展点初始化或者查找失败时，使用异常
 * <p>
 * 扩展点初始化或者查找失败时，使用异常
 * <p>
 *
 
 * @since 1.0.0 2022/11/18
 */
public class BizRuntimeException extends RuntimeException {

    private String errCode;

    public BizRuntimeException(String errMessage) {
        super(errMessage);
    }

    public BizRuntimeException(String errCode, String errMessage) {
        super(errMessage);
        this.errCode = errCode;
    }

    public BizRuntimeException(String errMessage, Throwable e) {
        super(errMessage, e);
    }

    public BizRuntimeException(String errCode, String errMessage, Throwable e) {
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
