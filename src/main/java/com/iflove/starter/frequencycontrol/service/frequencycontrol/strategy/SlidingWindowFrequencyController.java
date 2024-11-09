package com.iflove.starter.frequencycontrol.service.frequencycontrol.strategy;

import com.iflove.starter.frequencycontrol.domain.dto.FixedWindowDTO;
import com.iflove.starter.frequencycontrol.domain.dto.SlidingWindowDTO;
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
 * @implNote 滑动窗口实现
 */
@Slf4j
@Service
public class SlidingWindowFrequencyController extends AbstractFrequencyControlService<SlidingWindowDTO> {
    /**
     * 是否达到限流阈值
     *
     * @param frequencyControlMap 定义的注解频控
     *                            Map中的Key-{@link SlidingWindowDTO#getKey()}-对应redis的单个频控的Key
     *                            Map中的Value-对应redis的单个频控的Key限制的Value
     * @return true-方法被限流 false-方法没有被限流
     */
    @Override
    protected boolean reachRateLimit(Map<String, SlidingWindowDTO> frequencyControlMap) {
        List<String> frequencyKeys = new ArrayList<>(frequencyControlMap.keySet());
        for (String key : frequencyKeys) {
            SlidingWindowDTO dto = frequencyControlMap.get(key);
            // 获取滑动窗口内计数
            Long count = RedisUtil.zCard(dto.getKey());
            int frequencyControlCountLimit = dto.getCount();
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
     *                            Map中的Key-{@link SlidingWindowDTO#getKey()}-对应redis的单个频控的Key
     *                            Map中的Value-对应redis的单个频控的Key限制的Value
     */
    @Override
    protected void addFrequencyControlStatisticsCount(Map<String, SlidingWindowDTO> frequencyControlMap) {
        List<String> frequencyKeys = new ArrayList<>(frequencyControlMap.keySet());
        for (String key : frequencyKeys) {
            SlidingWindowDTO dto = frequencyControlMap.get(key);
            // 计算最小窗口周期，转换为毫秒级别
            long period = dto.getUnit().toMillis(dto.getPeriod());
            long current = System.currentTimeMillis();
            // 计算窗口大小，也是过期时间
            long length = period * dto.getWindowSize();
            long start = current - length;
            // 添加当前时间，同时删除窗口大小外的旧数据
            RedisUtil.zAddAndExpire(key, start, length, current);
        }
    }

    /**
     * 获取策略类枚举
     * @return {@link FrequencyControlStrategyEnum#SLIDING_WINDOW}
     */
    @Override
    protected FrequencyControlStrategyEnum getStrategyEnum() {
        return FrequencyControlStrategyEnum.SLIDING_WINDOW;
    }
}
