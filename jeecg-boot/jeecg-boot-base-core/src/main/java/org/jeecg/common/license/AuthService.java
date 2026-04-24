package org.jeecg.common.license;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AuthService {

    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqGKukO1De7zhZj6+H0qtjTkVxwTCpvKe4eCZ0FPqri0cb2JZfXJ/DgYSF6vUpwmJG8wVQZKjeGcjDOL5UlsuusFncCzWBQ7RKNUSesmQRMSGkVb1/3j+skZ6UtW+5u09lHNsj6tQ51s1SPrCBkedbNf0Tp0GbMJDyR4e9T04ZZwIDAQAB";
    
    private static final int TRIAL_DAYS = 30;
    
    private static final String AUTH_DIR = ".jeecg_auth";
    private static final String AUTH_FILE = "auth.dat";
    private static final String BACKUP_FILE = "auth.bak";
    
    private static volatile AuthInfo currentAuth;
    private static volatile long lastCheckTime = 0;
    private static volatile boolean authValid = true;

    @PostConstruct
    public void init() {
        try {
            loadAuth();
            checkAuth();
        } catch (Exception e) {
            log.error("授权初始化失败", e);
        }
    }

    public boolean isAuthValid() {
        checkAuth();
        return authValid;
    }

    public String getAuthStatus() {
        if (authValid) {
            if (currentAuth != null && currentAuth.getLicenseKey() != null) {
                return "已授权";
            }
            long remaining = getRemainingDays();
            return "试用中(剩余" + remaining + "天)";
        }
        return "授权已过期";
    }

    public long getRemainingDays() {
        if (currentAuth == null || currentAuth.getEndTime() == null) {
            return 0;
        }
        long diff = currentAuth.getEndTime().getTime() - System.currentTimeMillis();
        return Math.max(0, TimeUnit.MILLISECONDS.toDays(diff));
    }

    public boolean activateLicense(String licenseKey) {
        try {
            if (verifyLicense(licenseKey)) {
                currentAuth.setLicenseKey(licenseKey);
                currentAuth.setEndTime(null);
                currentAuth.setAuthStatus(1);
                saveAuth();
                authValid = true;
                log.info("授权激活成功");
                return true;
            }
        } catch (Exception e) {
            log.error("授权激活失败", e);
        }
        return false;
    }

    private synchronized void checkAuth() {
        long now = System.currentTimeMillis();
        
        if (now - lastCheckTime < 60000) {
            return;
        }
        
        lastCheckTime = now;
        
        try {
            if (currentAuth == null) {
                initTrial();
                authValid = true;
                return;
            }
            
            if (currentAuth.getLastCheckTime() != null && 
                currentAuth.getLastCheckTime().getTime() > now) {
                log.warn("检测到时间回拨，授权已失效");
                authValid = false;
                return;
            }
            
            currentAuth.setLastCheckTime(new Date(now));
            
            if (currentAuth.getLicenseKey() != null) {
                if (verifyLicense(currentAuth.getLicenseKey())) {
                    saveAuth();
                    authValid = true;
                    return;
                }
            }
            
            if (currentAuth.getEndTime() != null && now <= currentAuth.getEndTime().getTime()) {
                saveAuth();
                authValid = true;
                return;
            }
            
            authValid = false;
            
        } catch (Exception e) {
            log.error("授权检查失败", e);
            authValid = false;
        }
    }

    private void initTrial() {
        log.info("初始化30天试用期");
        currentAuth = new AuthInfo();
        currentAuth.setMachineId(getMachineId());
        currentAuth.setStartTime(new Date());
        currentAuth.setEndTime(new Date(System.currentTimeMillis() + TRIAL_DAYS * 24 * 3600 * 1000L));
        currentAuth.setLastCheckTime(new Date());
        currentAuth.setTrialDays(TRIAL_DAYS);
        currentAuth.setAuthStatus(0);
        saveAuth();
    }

    private void saveAuth() {
        try {
            String json = JSON.toJSONString(currentAuth);
            String encoded = oConvertUtils.encodeBase64(json.getBytes(StandardCharsets.UTF_8));
            
            File authDir = getAuthDir();
            File authFile = new File(authDir, AUTH_FILE);
            File backupFile = new File(authDir, BACKUP_FILE);
            
            FileUtil.writeString(encoded, authFile, StandardCharsets.UTF_8);
            FileUtil.writeString(encoded, backupFile, StandardCharsets.UTF_8);
            
        } catch (Exception e) {
            log.error("保存授权信息失败", e);
        }
    }

    private void loadAuth() {
        try {
            File authDir = getAuthDir();
            File authFile = new File(authDir, AUTH_FILE);
            File backupFile = new File(authDir, BACKUP_FILE);
            
            String content = null;
            if (authFile.exists()) {
                content = FileUtil.readString(authFile, StandardCharsets.UTF_8);
            }
            if (content == null && backupFile.exists()) {
                content = FileUtil.readString(backupFile, StandardCharsets.UTF_8);
            }
            
            if (content != null) {
                String decoded = new String(oConvertUtils.decodeBase64(content), StandardCharsets.UTF_8);
                currentAuth = JSON.parseObject(decoded, AuthInfo.class);
                log.info("授权信息加载成功");
            }
        } catch (Exception e) {
            log.error("加载授权信息失败", e);
            currentAuth = null;
        }
    }

    private boolean verifyLicense(String licenseKey) {
        try {
            RSA rsa = new RSA(null, PUBLIC_KEY);
            String decoded = new String(rsa.decrypt(licenseKey, KeyType.PublicKey), StandardCharsets.UTF_8);
            LicenseInfo info = JSON.parseObject(decoded, LicenseInfo.class);
            
            if (info.getMachineId() != null && !info.getMachineId().equals(getMachineId())) {
                log.warn("机器码不匹配");
                return false;
            }
            
            if (info.getExpireTime() != null && System.currentTimeMillis() > info.getExpireTime()) {
                log.warn("授权已过期");
                return false;
            }
            
            return true;
        } catch (Exception e) {
            log.error("授权验证失败", e);
            return false;
        }
    }

    private File getAuthDir() {
        String userHome = System.getProperty("user.home");
        File authDir = new File(userHome, AUTH_DIR);
        if (!authDir.exists()) {
            authDir.mkdirs();
            try {
                Runtime.getRuntime().exec("attrib +h " + authDir.getAbsolutePath());
            } catch (Exception e) {
            }
        }
        return authDir;
    }

    private String getMachineId() {
        try {
            StringBuilder sb = new StringBuilder();
            try {
                InetAddress addr = InetAddress.getLocalHost();
                sb.append(addr.getHostName());
                sb.append(System.getProperty("os.arch"));
            } catch (Exception e) {
            }
            sb.append(System.getProperty("java.version"));
            sb.append(Runtime.getRuntime().availableProcessors());
            return cn.hutool.crypto.digest.MD5.create().digestHex(sb.toString());
        } catch (Exception e) {
            return "UNKNOWN_MACHINE";
        }
    }

    public static class AuthInfo {
        private String machineId;
        private Date startTime;
        private Date endTime;
        private Date lastCheckTime;
        private Integer trialDays;
        private String licenseKey;
        private Integer authStatus;

        public String getMachineId() { return machineId; }
        public void setMachineId(String machineId) { this.machineId = machineId; }
        public Date getStartTime() { return startTime; }
        public void setStartTime(Date startTime) { this.startTime = startTime; }
        public Date getEndTime() { return endTime; }
        public void setEndTime(Date endTime) { this.endTime = endTime; }
        public Date getLastCheckTime() { return lastCheckTime; }
        public void setLastCheckTime(Date lastCheckTime) { this.lastCheckTime = lastCheckTime; }
        public Integer getTrialDays() { return trialDays; }
        public void setTrialDays(Integer trialDays) { this.trialDays = trialDays; }
        public String getLicenseKey() { return licenseKey; }
        public void setLicenseKey(String licenseKey) { this.licenseKey = licenseKey; }
        public Integer getAuthStatus() { return authStatus; }
        public void setAuthStatus(Integer authStatus) { this.authStatus = authStatus; }
    }

    public static class LicenseInfo {
        private String machineId;
        private Long expireTime;
        private String customer;
        private String version;

        public String getMachineId() { return machineId; }
        public void setMachineId(String machineId) { this.machineId = machineId; }
        public Long getExpireTime() { return expireTime; }
        public void setExpireTime(Long expireTime) { this.expireTime = expireTime; }
        public String getCustomer() { return customer; }
        public void setCustomer(String customer) { this.customer = customer; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
    }
}
