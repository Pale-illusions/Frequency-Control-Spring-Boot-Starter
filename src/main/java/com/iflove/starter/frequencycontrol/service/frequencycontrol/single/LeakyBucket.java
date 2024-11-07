package com.iflove.starter.frequencycontrol.service.frequencycontrol.single;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 漏桶算法
 */

public class LeakyBucket {
    /**
     * 桶的容量
     */
    private final int capacity;
    /**
     * 出桶速率 (每秒允许的请求数)
     */
    private final int rate;
    /**
     * 上一个请求时间戳
     */
    private long lastRequestTime;

    public LeakyBucket(int capacity, int rate) {
        this.capacity = capacity;
        this.rate = rate;
        this.lastRequestTime = System.currentTimeMillis();
    }

    public synchronized  long tryAcquire() {
        // 获取当前时间
        long currentTime = System.currentTimeMillis();
        // 判断：漏桶为空
        if (currentTime > lastRequestTime) {
            lastRequestTime = currentTime;
            return 0;
        }
        // 上次取水的间隔时间
        long elapsedTime = lastRequestTime - currentTime;
        // 计算桶中的水量
        int water = (int) elapsedTime * rate / 1000;
        // 水量不超过容量
        if (water < capacity) {
            long sleepTime = (1000 / rate) + elapsedTime;
            lastRequestTime = currentTime + sleepTime;
            return sleepTime;
        }
        // 拒绝请求
        return -1;
    }
}
