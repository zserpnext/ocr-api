package com.nmw.ocrapi.exception;

import com.nmw.ocrapi.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static com.nmw.ocrapi.exception.ExceptionEnum.PARAM_ERROR;

/**
 * @author :ljq
 * @date :2023/11/27
 * @description:全局的异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseResult serviceExceptionHandler(ServiceException e) {
        return ResponseResult.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e) {
        log.error("发生异常，异常信息：", e);
        return ResponseResult.error(e.getMessage());
    }

    /**
     * 参数校验异常处理
     */
    @ExceptionHandler(BindException.class)
    public ResponseResult validatedBindException(BindException e) {
        log.warn("参数校验异常:{}", e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return ResponseResult.error(PARAM_ERROR.getCode(),message);
    }

    /**
     * 参数校验异常处理
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseResult argumentNotValidErrorHandler(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            stringBuilder.append(fieldError.getDefaultMessage());
            log.warn("参数校验异常:{}", fieldError.getDefaultMessage());
            break;
        }
        return ResponseResult.error(PARAM_ERROR.getCode(), stringBuilder.toString());
    }
}
