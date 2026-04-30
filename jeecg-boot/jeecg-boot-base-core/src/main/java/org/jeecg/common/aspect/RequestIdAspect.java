package org.jeecg.common.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jeecg.common.util.RequestIdContext;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.stereotype.Component;

/**
 * 请求链路追踪切面
 * 在所有Controller请求进入时生成requestId，并在请求结束时清除
 */
@Slf4j
@Aspect
@Component
public class RequestIdAspect {

    @Pointcut("execution(public * org.jeecg.modules..controller..*.*(..))")
    public void controllerPointcut() {
    }

    @Around("controllerPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        
        String requestId = request.getHeader(RequestIdContext.getRequestIdHeader());
        
        if (requestId == null || requestId.isEmpty()) {
            requestId = RequestIdContext.generateRequestId();
        }
        
        RequestIdContext.setRequestId(requestId);
        
        log.info("RequestIdAspect - requestId: {}, uri: {}, method: {}", 
                requestId, request.getRequestURI(), request.getMethod());
        
        try {
            return joinPoint.proceed();
        } finally {
            RequestIdContext.clear();
        }
    }
}