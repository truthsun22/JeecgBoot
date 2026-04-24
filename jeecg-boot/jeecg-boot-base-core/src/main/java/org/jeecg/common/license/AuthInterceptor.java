package org.jeecg.common.license;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final String LOGIN_PATH = "/sys/login";
    private static final String LOGIN_HTML = "/login.html";
    private static final String[] EXCLUDE_PATHS = {
        "/sys/login",
        "/sys/randomImage",
        "/login.html",
        "/favicon.ico",
        "/error",
        "/static/",
        "/assets/",
        "/css/",
        "/js/",
        "/images/",
        "/fonts/"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getServletPath();
        
        if (shouldSkip(path)) {
            return true;
        }
        
        AuthService authService = getAuthService();
        if (authService == null) {
            return true;
        }
        
        boolean valid = authService.isAuthValid();
        if (!valid) {
            handleAuthExpired(request, response);
            return false;
        }
        
        return true;
    }

    private boolean shouldSkip(String path) {
        if (path == null) {
            return true;
        }
        for (String exclude : EXCLUDE_PATHS) {
            if (path.startsWith(exclude)) {
                return true;
            }
        }
        return false;
    }

    private AuthService getAuthService() {
        try {
            return SpringContextUtils.getBean(AuthService.class);
        } catch (Exception e) {
            log.warn("获取AuthService失败", e);
            return null;
        }
    }

    private void handleAuthExpired(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String accept = request.getHeader("Accept");
        String requestedWith = request.getHeader("X-Requested-With");
        
        boolean isAjax = "XMLHttpRequest".equals(requestedWith);
        boolean isJson = accept != null && accept.contains("application/json");
        
        if (isAjax || isJson) {
            response.setStatus(403);
            response.setContentType("application/json;charset=UTF-8");
            Result<Object> result = Result.error("系统授权已过期，请联系管理员获取授权");
            result.setCode(40301);
            response.getWriter().write(com.alibaba.fastjson.JSON.toJSONString(result));
        } else {
            response.setContentType("text/html;charset=UTF-8");
            String html = buildExpiredHtml();
            response.getWriter().write(html);
        }
    }

    private String buildExpiredHtml() {
        return "<!DOCTYPE html><html><head><meta charset='UTF-8'>"
            + "<title>系统授权已过期</title>"
            + "<style>body{font-family:Arial,Microsoft YaHei;background:#f5f5f5;display:flex;justify-content:center;align-items:center;height:100vh;margin:0;}"
            + ".container{background:white;padding:50px;border-radius:10px;box-shadow:0 2px 20px rgba(0,0,0,0.1);text-align:center;max-width:500px;}"
            + ".icon{font-size:80px;margin-bottom:20px;}"
            + "h1{color:#dc3545;margin-bottom:20px;}"
            + "p{color:#666;line-height:1.8;font-size:16px;}"
            + ".contact{margin-top:30px;padding:20px;background:#f8f9fa;border-radius:5px;}"
            + "</style></head><body>"
            + "<div class='container'>"
            + "<div class='icon'>⚠️</div>"
            + "<h1>系统授权已过期</h1>"
            + "<p>您的系统试用期已结束或授权已过期。</p>"
            + "<p>请联系系统管理员获取授权码以继续使用。</p>"
            + "<div class='contact'>"
            + "<p><strong>联系方式</strong></p>"
            + "<p>如需授权，请联系供应商</p>"
            + "</div>"
            + "</div></body></html>";
    }
}
