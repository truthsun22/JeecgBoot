package org.jeecg.common.util;

import java.util.UUID;

/**
 * requestId上下文工具类，用于存储和管理请求链路追踪ID
 */
public class RequestIdContext {

    private static final ThreadLocal<String> REQUEST_ID = new ThreadLocal<>();

    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    private RequestIdContext() {
    }

    /**
     * 获取当前请求的requestId
     *
     * @return requestId
     */
    public static String getRequestId() {
        return REQUEST_ID.get();
    }

    /**
     * 设置requestId
     *
     * @param requestId 请求ID
     */
    public static void setRequestId(String requestId) {
        REQUEST_ID.set(requestId);
    }

    /**
     * 生成一个新的requestId
     *
     * @return 新生成的requestId
     */
    public static String generateRequestId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 清除requestId，防止内存泄漏
     */
    public static void clear() {
        REQUEST_ID.remove();
    }

    /**
     * 获取requestId的HTTP头名称
     *
     * @return 头名称
     */
    public static String getRequestIdHeader() {
        return REQUEST_ID_HEADER;
    }
}