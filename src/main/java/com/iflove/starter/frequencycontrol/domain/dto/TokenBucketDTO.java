package com.iflove.starter.frequencycontrol.domain.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 令牌桶
 */
@Slf4j
@Data
public class TokenBucketDTO extends FrequencyControlDTO {
    /**
     * 令牌桶容量
     */
    private final long capacity;
    /**
     * 令牌产生速率 (每秒的令牌数)
     */
    private final double rate;
    /**
     * 当前令牌数量
     */
    private double tokens;
    /**
     * 上次令牌补充时间
     */
    private long lastRefillTime;

    private final ReentrantLock lock = new ReentrantLock();

    public TokenBucketDTO(long capacity, double rate) {
        if (capacity <= 0 || rate <= 0) {
            throw new IllegalArgumentException("参数值需大于0");
        }
        this.capacity = capacity;
        this.rate = rate;
        this.tokens = capacity;
        this.lastRefillTime = System.nanoTime();
    }

    public boolean tryAcquire(int permits) {
        lock.lock();
        try {
            refillTokens();
            if (tokens < permits) {
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public void deductionToken(int permits) {
        lock.lock();
        try {
            tokens -= permits;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 补充令牌
     */
    private void refillTokens() {
        long currentTime = System.nanoTime();
        // 转换为秒
        double elapsedTime = (currentTime - lastRefillTime) / 1e9;
        double tokensToAdd = elapsedTime * rate;
        log.info("tokensToAdd is {}", tokensToAdd);
        // 令牌总数不能超过令牌桶容量
        tokens = Math.min(capacity, tokens + tokensToAdd);
        log.info("current tokens is {}", tokens);
        lastRefillTime = currentTime;
    }
}
