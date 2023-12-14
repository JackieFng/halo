package com.ppy.halo.exception;

/**
 * 扩展点初始化或者查找失败时，使用次异常
 * <p>
 * 扩展点初始化或者查找失败时，使用次异常
 * <p>
 *
 * @author Luke
 * @version 1.0.0
 * @since 1.0.0 2022/11/18
 */
public class ScenarioException extends RuntimeException {

    private String errCode;

    public ScenarioException(String errMessage) {
        super(errMessage);
    }

    public ScenarioException(String errCode, String errMessage) {
        super(errMessage);
        this.errCode = errCode;
    }

    public ScenarioException(String errMessage, Throwable e) {
        super(errMessage, e);
    }

    public ScenarioException(String errCode, String errMessage, Throwable e) {
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
