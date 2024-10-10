package com.demo.common.exception;

import com.demo.common.core.result.IResultCode;

public class BizException extends RuntimeException{

    public IResultCode resultCode;

    public BizException(IResultCode errorCode) {
        super(errorCode.getMsg());
        this.resultCode = errorCode;
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public IResultCode getResultCode() {
        return this.resultCode;
    }
}
