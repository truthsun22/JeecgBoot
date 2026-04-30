package org.jeecg.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.RequestIdContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 请求链路追踪过滤器
 * 在请求进入时生成或获取requestId，并在响应头中返回
 */
@Slf4j
@Component
@Order(1)
public class RequestIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) 
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        
        String requestId = request.getHeader(RequestIdContext.getRequestIdHeader());
        
        if (requestId == null || requestId.isEmpty()) {
            requestId = RequestIdContext.generateRequestId();
        }
        
        RequestIdContext.setRequestId(requestId);
        
        jakarta.servlet.http.HttpServletResponse response = (jakarta.servlet.http.HttpServletResponse) servletResponse;
        response.setHeader(RequestIdContext.getRequestIdHeader(), requestId);
        
        log.debug("RequestIdFilter - requestId: {}, uri: {}", requestId, request.getRequestURI());
        
        try {
            filterChain.doFilter(request, response);
        } finally {
            RequestIdContext.clear();
        }
    }
}