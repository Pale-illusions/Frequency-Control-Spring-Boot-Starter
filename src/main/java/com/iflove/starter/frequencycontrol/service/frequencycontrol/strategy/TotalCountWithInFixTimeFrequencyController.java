package com.iflove.starter.frequencycontrol.service.frequencycontrol.strategy;

import com.iflove.starter.frequencycontrol.domain.dto.FixedWindowDTO;
import com.iflove.starter.frequencycontrol.domain.enums.FrequencyControlStrategyEnum;
import com.iflove.starter.frequencycontrol.service.frequencycontrol.AbstractFrequencyControlService;
import com.iflove.starter.frequencycontrol.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 固定窗口实现
 */
@Slf4j
@Service
public class TotalCountWithInFixTimeFrequencyController extends AbstractFrequencyControlService<FixedWindowDTO> {
    /**
     * 是否达到限流阈值
     *
     * @param frequencyControlMap 定义的注解频控
     *                            Map中的Key-{@link FixedWindowDTO#getKey()}-对应redis的单个频控的Key
     *                            Map中的Value-对应redis的单个频控的Key限制的Value
     * @return true-方法被限流 false-方法没有被限流
     */
    @Override
    protected boolean reachRateLimit(Map<String, FixedWindowDTO> frequencyControlMap) {
        // 批量获取
        List<String> frequencyKeys = new ArrayList<>(frequencyControlMap.keySet());
        List<Integer> countList = RedisUtil.mget(frequencyKeys, Integer.class);
        for (int i = 0; i < frequencyKeys.size(); i++) {
            String key = frequencyKeys.get(i);
            Integer count = countList.get(i);
            int frequencyControlCountLimit = frequencyControlMap.get(key).getCount();
            // 判断：到达限流阈值
            if (Objects.nonNull(count) && count >= frequencyControlCountLimit) {
                log.warn("frequenctControl limit key:{}, count:{}", key, count);
                return true;
            }
        }
        return false;
    }

    /**
     * 增加限流统计次数
     *
     * @param frequencyControlMap 定义的注解频控
     *                            Map中的Key-{@link FixedWindowDTO#getKey()}-对应redis的单个频控的Key
     *                            Map中的Value-对应redis的单个频控的Key限制的Value
     */
    @Override
    protected void addFrequencyControlStatisticsCount(Map<String, FixedWindowDTO> frequencyControlMap) {
        frequencyControlMap.forEach((k, v) -> RedisUtil.inc(k, v.getTime(), v.getUnit()));
    }

    /**
     * 获取策略类枚举
     * @return {@link FrequencyControlStrategyEnum#TOTAL_COUNT_WITH_IN_FIX_TIME}
     */
    @Override
    protected FrequencyControlStrategyEnum getStrategyEnum() {
        return FrequencyControlStrategyEnum.TOTAL_COUNT_WITH_IN_FIX_TIME;
    }
}
