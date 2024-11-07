package com.iflove.starter.frequencycontrol.service.frequencycontrol.single;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 令牌桶算法
 */

public class TokenBucket {
    /**
     * 令牌桶容量
     */
    private final int capacity;
    /**
     * 令牌产生速率 (每秒的令牌数)
     */
    private final int rate;
    /**
     * 当前令牌数量
     */
    private int tokens;
    /**
     * 上次令牌补充时间
     */
    private long lastRefillTime;

    private long left = 0;

    public TokenBucket(int capacity, int rate) {
        this.capacity = capacity;
        this.rate = rate;
        this.tokens = capacity;
        this.lastRefillTime = System.currentTimeMillis();
    }

    public synchronized boolean tryAcquire() {
        refillTokens();
        if (tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }

    /**
     * 补充令牌
     */
    private void refillTokens() {
        // 获取当前系统时间
        long currentTime = System.currentTimeMillis();
        // 当前时间和上次请求时间的时间差
        long elaspedTime = currentTime - lastRefillTime;

        if (elaspedTime > 0) {
            // 需要补充的令牌数量
            int newTokens = (int) ((elaspedTime * rate + left) / 1000);
            left = (elaspedTime * rate + left) % 1000;
            // 令牌不能超过桶的大小
            tokens = Math.min(tokens + newTokens, capacity);
            lastRefillTime = currentTime;
        }
    }
}
