package com.iflove.starter.frequencycontrol.service.frequencycontrol;

import com.iflove.starter.frequencycontrol.domain.dto.FrequencyControlDTO;
import com.iflove.starter.frequencycontrol.domain.enums.FrequencyControlStrategyEnum;
import com.iflove.starter.frequencycontrol.exception.ClientException;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 限流工具类
 */

public class FrequencyControlUtil {
    /**
     * 单限流策略的调用方法-编程式调用
     *
     * @param strategy     策略枚举
     * @param frequencyControl 单个频控对象
     */
    public static <K extends FrequencyControlDTO> void executeWithFrequencyControl(FrequencyControlStrategyEnum strategy, K frequencyControl) {
        AbstractFrequencyControlService<K> frequencyController = FrequencyControlStrategyFactory.getStrategy(strategy.getCode());
        frequencyController.executeWithFrequencyControl(frequencyControl);
    }

    /**
     * 多限流策略的编程式调用方法调用方法
     *
     * @param strategy         策略枚举
     * @param frequencyControlList 频控列表 包含每一个频率控制的定义以及顺序
     */
    public static <K extends FrequencyControlDTO> void executeWithFrequencyControlList(FrequencyControlStrategyEnum strategy, List<K> frequencyControlList) {
        AbstractFrequencyControlService<K> frequencyController = FrequencyControlStrategyFactory.getStrategy(strategy.getCode());
        frequencyController.executeWithFrequencyControlList(frequencyControlList);
    }
}
