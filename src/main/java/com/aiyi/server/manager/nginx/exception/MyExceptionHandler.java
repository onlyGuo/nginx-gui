package com.aiyi.server.manager.nginx.exception;

import com.aiyi.server.manager.nginx.bean.result.Result;
import com.aiyi.server.manager.nginx.common.CommonFields;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(HttpServletResponse res, Exception e) {
        res.setStatus(500);
        Result result = new Result();
        result.setSuccess(false);
        String message = e.getMessage();
        if (null == message || StringUtils.isEmpty(message)) {
            message = "内部错误";
        }
        result.setMessage(message);
        if (e instanceof NginxServiceManagerException) {
            result.setCode(CommonFields.ERROR_CODE.NGINX);
        } else if (e instanceof ValiException || e instanceof ValidationException) {
            res.setStatus(400);
            result.setCode(CommonFields.ERROR_CODE.VALIDATION);
        } else {
            result.setCode(CommonFields.ERROR_CODE.SERVER);
        }
        return result;
    }
}
