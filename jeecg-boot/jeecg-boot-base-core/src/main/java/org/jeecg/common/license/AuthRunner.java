package org.jeecg.common.license;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class AuthRunner implements ApplicationRunner {

    @Autowired
    private AuthService authService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            boolean valid = authService.isAuthValid();
            String status = authService.getAuthStatus();
            
            log.info("========================================");
            log.info("      系统授权验证");
            log.info("========================================");
            log.info("授权状态: {}", status);
            log.info("验证结果: {}", valid ? "有效" : "已过期");
            
            if (!valid) {
                log.warn("========================================");
                log.warn("      警告：系统授权已过期!");
                log.warn("========================================");
                log.warn("系统试用期已结束或授权已过期。");
                log.warn("请联系管理员获取授权码以继续使用。");
                log.warn("========================================");
            } else {
                log.info("授权验证通过，系统正常运行。");
            }
            log.info("========================================");
            
        } catch (Exception e) {
            log.error("授权验证启动失败", e);
        }
    }
}
