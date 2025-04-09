package com.lgcns.admin.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Pointcut("within(com.lgcns.admin..controller.*)") // 패키지 범위 설정
    public void controller() {}

    private Map<String, Object> getParams(JoinPoint joinPoint) {
        Map<String, Object> paramMap = new HashMap<>();
        String paramName = null;
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        for(int index = 0; index < method.getParameters().length; index++) {
            paramName = method.getParameters()[index].getName();
            paramMap.put(paramName, args[index]);
        }

        return paramMap;
    }

    @Before("controller()")
    public void beforeRequest(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String controllerName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = ((MethodSignature) joinPoint.getSignature()).getName();

        Map<String, Object> paramsMap = new HashMap<>();
        String uri = request.getRequestURI();

        paramsMap.put("controller", controllerName);
        paramsMap.put("method", methodName);
        paramsMap.put("params", getParams(joinPoint));
        paramsMap.put("requestUri", uri);
        paramsMap.put("httpMethod", request.getMethod());
        paramsMap.put("session", BaseController.getSessionInfo(request));

        log.info("[{}] [{}] {}", paramsMap.get("session"), paramsMap.get("httpMethod"), paramsMap.get("requestUri"));
        log.info("[{}] [method] {}.{}", paramsMap.get("session"), paramsMap.get("controller"), paramsMap.get("method"));
        log.info("[{}] [params] {}", paramsMap.get("session"), paramsMap.get("params"));
    }

    @AfterReturning(pointcut = "controller()", returning = "returnValue")
    public void afterRequest(JoinPoint joinPoint, Object returnValue) {
        if (returnValue == null) return;

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String session = BaseController.getSessionInfo(request);

        log.info("[{}] {}", session, returnValue);

    }

    @AfterThrowing(pointcut = "controller()", throwing = "e")
    public void afterThrowingLogging(JoinPoint joinPoint, Exception e) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String session = BaseController.getSessionInfo(request);

        log.error("[{}] {}", session, e.getMessage(), e);
    }
}
