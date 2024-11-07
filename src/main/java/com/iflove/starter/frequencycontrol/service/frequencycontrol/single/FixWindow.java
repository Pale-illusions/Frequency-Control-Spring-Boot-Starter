package com.iflove.starter.frequencycontrol.service.frequencycontrol.single;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 固定窗口算法
 */

public class FixWindow {
    /**
     * 当前窗口累计请求数
     */
    private Integer count;
    /**
     * 最后一次请求时间
     */
    private long lastAcquireTime;
    /**
     * 固定窗口时间区间 (毫秒)
     */
    private final Long windowInMills;
    /**
     * 最大请求限制
     */
    private final Integer maxRequests;

    public FixWindow(Long windowInMills, Integer maxRequests) {
        this.windowInMills = windowInMills;
        this.maxRequests = maxRequests;
    }

    public synchronized boolean tryAcquire() {
        long currentTime = System.currentTimeMillis(); // 获取当前系统时间
        // 当前和上次不在同一时间窗口
        if (currentTime - lastAcquireTime > windowInMills) {
            count = 0; // 计数器清0
            lastAcquireTime = currentTime; // 开启新的时间窗口
        } else { // 在同一窗口内
            if (count < maxRequests) { // 小于阈值
                count++;
                return true;
            }
        }
        return false;
    }
}
