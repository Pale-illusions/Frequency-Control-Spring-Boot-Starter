package com.iflove.starter.frequencycontrol.service.frequencycontrol.strategy;

import com.iflove.starter.frequencycontrol.domain.dto.SlidingWindowDTO;
import com.iflove.starter.frequencycontrol.domain.dto.TokenBucketDTO;
import com.iflove.starter.frequencycontrol.domain.enums.FrequencyControlStrategyEnum;
import com.iflove.starter.frequencycontrol.manager.TokenBucketManager;
import com.iflove.starter.frequencycontrol.service.frequencycontrol.AbstractFrequencyControlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 令牌桶实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenBucketFrequencyController extends AbstractFrequencyControlService<TokenBucketDTO> {
    private final TokenBucketManager tokenBucketManager;

    /**
     * 是否达到限流阈值
     *
     * @param frequencyControlMap 定义的注解频控
     *                            Map中的Key-{@link TokenBucketDTO#getKey()}-对应redis的单个频控的Key
     *                            Map中的Value-对应redis的单个频控的Key限制的Value
     * @return true-方法被限流 false-方法没有被限流
     */
    @Override
    protected boolean reachRateLimit(Map<String, TokenBucketDTO> frequencyControlMap) {
        List<String> frequencyKeys = new ArrayList<>(frequencyControlMap.keySet());
        for (String key : frequencyKeys) {
            // 尝试获取1个令牌(不扣减)，如果失败，说明令牌为空，需要限流
            if (!tokenBucketManager.tryAcquire(key, 1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 增加限流统计次数
     *
     * @param frequencyControlMap 定义的注解频控
     *                            Map中的Key-{@link TokenBucketDTO#getKey()}-对应redis的单个频控的Key
     *                            Map中的Value-对应redis的单个频控的Key限制的Value
     */
    @Override
    protected void addFrequencyControlStatisticsCount(Map<String, TokenBucketDTO> frequencyControlMap) {
        List<String> frequencyKeys = new ArrayList<>(frequencyControlMap.keySet());
        for (String key : frequencyKeys) {
            TokenBucketDTO dto = frequencyControlMap.get(key);
            // 创建令牌桶，如果不存在
            tokenBucketManager.createTokenBucket(key, dto.getCapacity(), dto.getRate());
            // 扣减一个令牌
            tokenBucketManager.deductionToken(key, 1);
        }
    }

    /**
     * 获取策略类枚举
     * @return {@link FrequencyControlStrategyEnum#TOKEN_BUCKET}
     */
    @Override
    protected FrequencyControlStrategyEnum getStrategyEnum() {
        return FrequencyControlStrategyEnum.TOKEN_BUCKET;
    }
}
