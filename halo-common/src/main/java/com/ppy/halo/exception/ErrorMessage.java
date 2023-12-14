package com.ppy.halo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 应用错误枚举定义类
 * @author jackie
 * @since 1.0.0 2022/11/18
 */
@AllArgsConstructor
@Getter
public enum ErrorMessage {

    EXTENSION_REGISTER_RETURN_TYPE_ERROR("EXTENSION_REGISTER_RETURN_TYPE_ERROR", "[%s] return type is required and must assign from interface [ExtensionPointI]"),

    EXTENSION_REGISTER_PARAMETER_ERROR("EXTENSION_REGISTER_PARAMETER_ERROR", "[%s] parameter should be null"),

    EXTENSION_REGISTER_METHOD_INVOKE_ERROR("EXTENSION_REGISTER_METHOD_INVOKE_ERROR", "[%s] method invoke error"),

    EXTENSION_REGISTER_RETURN_VALUE_ERROR("EXTENSION_REGISTER_RETURN_VALUE_ERROR", "[%s] return value should not be null"),

    EXTENSION_REGISTER_DUPLICATE_ERROR("EXTENSION_REGISTER_DUPLICATE_ERROR", "[%s] SDK [%s] for product [%s] is already registered"),

    EXTENSION_REGISTER_INTERFACE_ERROR("EXTENSION_REGISTER_INTERFACE_ERROR", "SDK [%s] must be assigned from interface [ExtensionPointI]"),

    EXTENSION_NOT_FOUND_ERROR("EXTENSION_NOT_FOUND_ERROR", "can not find extension with ExtensionPoint: %s,BizScenario:%s"),

    EXTENSION_SCENARIO_NULL_ERROR("EXTENSION_SCENARIO_NULL_ERROR", "BizScenario can not be null for extension"),

    SCENARIO_ACTIVITY_DEFINE_ERROR("SCENARIO_ACTIVITY_DEFINE_ERROR", "activity null ,should first call start method to init."),

    DOMAIN_PROCESS_ERROR("DOMAIN_PROCESS_ERROR", "domain process error,service=%s,errorCode=%s,errorMsg=%s"),

    FILE_UPLOAD_ERROR("FILE_UPLOAD_ERROR", "file upload unknown error"),

    MKDIR_SPACE_ERROR("MKDIR_SPACE_ERROR", "mkdir space unknown error"),

    RM_SPACE_ERROR("RM_SPACE_ERROR", "rm space unknown error"),

    PARAMETER_NULL_ERROR("PARAMETER_NULL_ERROR", "parameter null error"),

    ITEM_IN_PROCESS_ERROR("ITEM_IN_PROCESS_ERROR", "item in process error"),

    NO_ACTIVE_TASK_ERROR("NO_ACTIVE_TASK_ERROR", "no active task error"),

    UNKNOWN_ERROR("UNKNOWN_ERROR", "unknown error caused by system error or others");

    private String code;

    private String message;

    public static String getMsg(String code) {
        for (ErrorMessage value : values()) {
            if (code == value.code) {
                return value.message;
            }
        }
        return null;
    }
}
