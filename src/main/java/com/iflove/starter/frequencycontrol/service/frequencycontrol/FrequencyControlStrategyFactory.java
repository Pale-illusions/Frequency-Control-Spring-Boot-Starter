package com.iflove.starter.frequencycontrol.service.frequencycontrol;

import com.iflove.starter.frequencycontrol.domain.dto.FrequencyControlDTO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 限流策略工厂
 */

public class FrequencyControlStrategyFactory {
    /**
     * 限流策略集合
     */
    private static final Map<Integer, AbstractFrequencyControlService<?>> STRATEGY_MAP = new ConcurrentHashMap<>(8);

    /**
     * 初始化策略工厂
     *
     * @param code 策略类型
     * @param strategy 策略类
     */
    public static <k extends FrequencyControlDTO> void register(Integer code, AbstractFrequencyControlService<k> strategy) {
        STRATEGY_MAP.put(code, strategy);
    }

    /**
     * 根据code获取策略类
     *
     * @param code 策略类型
     * @return 策略类
     */
    @SuppressWarnings("unchecked")
    public static <k extends FrequencyControlDTO> AbstractFrequencyControlService<k> getStrategy(Integer code) {
        return (AbstractFrequencyControlService<k>) STRATEGY_MAP.get(code);
    }
}
