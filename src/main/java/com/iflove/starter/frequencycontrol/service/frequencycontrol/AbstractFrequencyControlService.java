package com.iflove.starter.frequencycontrol.service.frequencycontrol;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.iflove.starter.frequencycontrol.domain.dto.FrequencyControlDTO;
import com.iflove.starter.frequencycontrol.domain.enums.FrequencyControlStrategyEnum;
import com.iflove.starter.frequencycontrol.exception.FrequencyControlException;
import com.iflove.starter.frequencycontrol.exception.ServiceException;
import io.netty.util.internal.StringUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 限流服务抽象类
 */
@Slf4j
public abstract class AbstractFrequencyControlService<K extends FrequencyControlDTO> {
    /**
     * 初始化注册策略类
     */
    @PostConstruct
    protected void init() {
        FrequencyControlStrategyFactory.register(getStrategyEnum().getCode(), this);
    }

    /**
     * 执行限流策略
     *
     * @param frequencyControlMap   定义的注解频控
     *                              Map中的Key-{@link K#getKey()}-对应redis的单个频控的Key
     *                              Map中的Value-对应redis的单个频控的Key限制的Value
     */
    private void executeWithFrequencyControlMap(Map<String, K> frequencyControlMap) {
        // 判断：是否达到限流阈值
        if (reachRateLimit(frequencyControlMap)) {
            throw new FrequencyControlException((String) null);
        }
        // 增加限流统计
        addFrequencyControlStatisticsCount(frequencyControlMap);
    }

    /**
     * 多限流策略的编程式调用
     *
     * @param frequencyControlList  频控列表
     */
    public void executeWithFrequencyControlList(List<K> frequencyControlList) {
        // 保证频控对象 key 不为空，否则报错
        boolean existsFrequencyControlHasNullKey = frequencyControlList.stream().anyMatch(frequencyControl -> StrUtil.isBlank(frequencyControl.getKey()));
        if (existsFrequencyControlHasNullKey) {
            throw new ServiceException("限流策略的Key字段不允许出现空值");
        }
        // 根据频控对象的key，生成键值映射
        Map<String, K> frequencyControlDTOMap = frequencyControlList.stream().collect(Collectors.groupingBy(K::getKey, Collectors.collectingAndThen(Collectors.toList(), list -> list.get(0))));
        executeWithFrequencyControlMap(frequencyControlDTOMap);
    }

    /**
     * 单限流策略的编程式调用
     *
     * @param frequencyControl  频控对象
     */
    public void executeWithFrequencyControl(K frequencyControl) {
        executeWithFrequencyControlList(Collections.singletonList(frequencyControl));
    }

    /**
     * 是否达到限流阈值 (子类实现)
     *
     * @param frequencyControlMap 定义的注解频控
     *                            Map中的Key-{@link K#getKey()}-对应redis的单个频控的Key
     *                            Map中的Value-对应redis的单个频控的Key限制的Value
     * @return true-方法被限流 false-方法没有被限流
     */
    protected abstract boolean reachRateLimit(Map<String, K> frequencyControlMap);

    /**
     * 增加限流统计次数 (子类实现)
     *
     * @param frequencyControlMap 定义的注解频控
     *                            Map中的Key-{@link K#getKey()}-对应redis的单个频控的Key
     *                            Map中的Value-对应redis的单个频控的Key限制的Value
     */
    protected abstract void addFrequencyControlStatisticsCount(Map<String, K> frequencyControlMap);

    /**
     * 获取策略类枚举
     * @return {@link FrequencyControlStrategyEnum}
     */
    protected abstract FrequencyControlStrategyEnum getStrategyEnum();
}
