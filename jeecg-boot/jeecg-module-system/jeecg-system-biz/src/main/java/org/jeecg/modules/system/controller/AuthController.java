package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.license.AuthService;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sys/auth")
@Api(tags = "系统授权管理")
@Slf4j
public class AuthController {

    @Autowired(required = false)
    private AuthService authService;

    @ApiOperation("获取授权状态")
    @GetMapping("/status")
    public Result<Map<String, Object>> getAuthStatus() {
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> data = new HashMap<>();
        
        if (authService == null) {
            data.put("valid", true);
            data.put("status", "未启用授权");
            data.put("remainingDays", -1);
            result.setResult(data);
            return result;
        }
        
        boolean valid = authService.isAuthValid();
        String status = authService.getAuthStatus();
        long remainingDays = authService.getRemainingDays();
        
        data.put("valid", valid);
        data.put("status", status);
        data.put("remainingDays", remainingDays);
        
        result.setResult(data);
        result.setSuccess(true);
        return result;
    }

    @ApiOperation("激活授权码")
    @PostMapping("/activate")
    public Result<String> activateLicense(@RequestBody JSONObject params) {
        String licenseKey = params.getString("licenseKey");
        
        if (oConvertUtils.isEmpty(licenseKey)) {
            return Result.error("授权码不能为空");
        }
        
        if (authService == null) {
            return Result.error("授权服务未启用");
        }
        
        boolean success = authService.activateLicense(licenseKey);
        
        if (success) {
            return Result.ok("授权激活成功");
        } else {
            return Result.error("授权码无效或已过期，请检查授权码");
        }
    }
}
