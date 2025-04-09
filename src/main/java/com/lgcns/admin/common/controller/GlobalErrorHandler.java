package com.lgcns.admin.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;

@Slf4j
@ControllerAdvice(basePackages = "com.lgcns.admin")
public class GlobalErrorHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public Object handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.debug("[{}] handleAccessDeniedException - Error: {}", BaseController.getSessionInfo(request), e.getMessage(), e);
        request.setAttribute("msg", e.getMessage());
        request.setAttribute("status", HttpStatus.FORBIDDEN.value());

        return "forward:/error/handling";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.debug("[{}] handleIllegalArgumentException - Error: {}",  BaseController.getSessionInfo(request), e.getMessage(), e);
        request.setAttribute("msg", e.getMessage());
        request.setAttribute("status", HttpStatus.BAD_REQUEST.value());

        return "forward:/error/handling";
    }

    @ExceptionHandler(Exception.class)
    public String handleAnyException(Exception e, HttpServletRequest request) {
        log.debug("[{}] handleAnyException - Error: {}",  BaseController.getSessionInfo(request), e.getMessage(), e);
        request.setAttribute("msg", e.getMessage());
        request.setAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return "forward:/error/handling";
    }
}
