package com.ricky.campus.common;

public enum ResultCode implements IErrorCode {
    SUCCESS(200,"操作成功"),
    FAILED(500,"操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    NOT_FOUND(404, "数据不存在");

    private final long code;
    private final String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Long getCode() { return code; }

    @Override
    public String getMessage() { return message; }
}
