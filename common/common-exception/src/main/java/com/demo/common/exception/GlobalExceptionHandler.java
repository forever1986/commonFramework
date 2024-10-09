package com.demo.common.exception;

import com.demo.common.core.result.Result;
import com.demo.common.core.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({RuntimeException.class})
    public <T> Result<T> handleRuntimeException(RuntimeException e) {
        log.error("兜底异常，异常原因：{}", e.getMessage(), e);
        return Result.failed(ResultCode.SYSTEM_EXECUTION_ERROR);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({BizException.class})
    public <T> Result<T> handleBizException(BizException e) {
        log.error("业务异常，异常原因：{}", e.getMessage(), e);
        return e.getResultCode() != null ? Result.failed(e.getResultCode()) : Result.failed(e.getMessage());
    }
}
