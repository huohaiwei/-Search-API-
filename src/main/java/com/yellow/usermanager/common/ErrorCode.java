package com.yellow.usermanager.common;

/**
 * 错误码
 */
public enum ErrorCode {

    SUCCESS(0,"ok",""),
    PARAMS_ERROR(40000,"请求参数错误",""),
    NULL_ERROR(40001,"请求数据为空",""),
    NO_LOGIN(40100,"未登录",""),
    NO_AUTH(40101,"无权限",""),
    /**
     * 框架产生的错误信息，让前端无法看到内部错误
     */
    SYSTEM_ERROR(50000,"系统内部异常","");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 状态码的含义
     */
    private final String massage;

    /**
     * 状态的详细描述
     */
    private final String description;

    ErrorCode(int code, String massage, String description) {
        this.code = code;
        this.massage = massage;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMassage() {
        return massage;
    }

    public String getDescription() {
        return description;
    }
}
