package com.iflove.starter.frequencycontrol.config.frequencycontrol;

import com.iflove.starter.frequencycontrol.domain.enums.FrequencyControlStrategyEnum;
import com.iflove.starter.frequencycontrol.domain.enums.FrequencyControlTargetEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 频控属性配置
 */
@ConfigurationProperties(prefix = "frequencycontrol")
@Getter
@Setter
public class FrequencyControlProperties {
    /**
     * 策略
     */
    private FrequencyControlStrategyEnum strategy = FrequencyControlStrategyEnum.TOTAL_COUNT_WITH_IN_FIX_TIME;

    /**
     * 窗口大小，默认 5 个 period
     */
    private int windowSize = 5;

    /**
     * 窗口最小周期，单位为秒，默认 1 秒
     */
    private int period = 1;

    /**
     * key 的前缀，默认取方法全限定名
     */
    private String prefixKey = "";

    /**
     * 频控对象，默认为 EL 表达式指定具体的频控对象
     */
    private FrequencyControlTargetEnum target = FrequencyControlTargetEnum.EL;

    /**
     * Spring EL 表达式，target=EL 必填
     */
    private String spEl = "";

    /**
     * 频控时间范围，单位秒，默认 10 秒
     */
    private int time = 10;

    /**
     * 频控时间单位，默认秒
     */
    private TimeUnit unit = TimeUnit.SECONDS;

    /**
     * 单位时间内最大访问次数
     */
    private int count = 10;

    /**
     * 令牌桶容量，默认 3
     */
    private long capacity = 3;

    /**
     * 每秒补充的令牌数，默认 0.5
     */
    private double refillRate = 0.5;
}
