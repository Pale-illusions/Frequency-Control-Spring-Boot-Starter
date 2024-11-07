package com.iflove.starter.frequencycontrol.service.frequencycontrol.single;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 滑动窗口算法
 */

public class SlideWindow {
    /**
     * 最大请求
     */
    private final int maxRequests;
    /**
     * 窗口范围
     */
    private final long windowInMills;
    /**
     * 请求时间戳队列
     */
    private Queue<Long> requestTimestamps;

    public SlideWindow(int maxRequests, long windowInMills) {
        this.maxRequests = maxRequests;
        this.windowInMills = windowInMills;
        requestTimestamps = new LinkedList<>();
    }

    public synchronized boolean tryAcquire() {
        long currentTime = System.currentTimeMillis(); // 当前系统时间
        // 清除过期窗口的请求
        cleanExpiredRequests(currentTime);
        // 判断：如果统计窗口内的请求数小于总限制
        if (requestTimestamps.size() < maxRequests) {
            requestTimestamps.offer(currentTime);
            return true;
        }
        return false;
    }

    private void cleanExpiredRequests(long currentTime) {
        // 判断：如果已存在的请求的时间戳超出时间窗口就移除
        while(!requestTimestamps.isEmpty() && (currentTime - requestTimestamps.peek() > windowInMills)) {
            requestTimestamps.poll();
        }
    }
}
