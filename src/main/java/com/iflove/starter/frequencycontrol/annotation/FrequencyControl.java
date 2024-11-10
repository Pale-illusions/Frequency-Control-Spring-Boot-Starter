package com.iflove.starter.frequencycontrol.annotation;

import com.iflove.starter.frequencycontrol.domain.enums.FrequencyControlStrategyEnum;
import com.iflove.starter.frequencycontrol.domain.enums.FrequencyControlTargetEnum;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 频控注解
 */

@Repeatable(FrequencyControlContainer.class) // 可重复
@Retention(RetentionPolicy.RUNTIME)// 运行时生效
@Target(ElementType.METHOD)//作用在方法上
public @interface FrequencyControl {
    /**
     * 策略
     * @see FrequencyControlStrategyEnum
     */
    FrequencyControlStrategyEnum strategy() default FrequencyControlStrategyEnum.TOTAL_COUNT_WITH_IN_FIX_TIME;

    /**
     * 窗口大小，默认 5 个 period
     */
    int windowSize() default -1;

    /**
     * 窗口最小周期 1s (窗口大小是 5s， 1s一个小格子，共10个格子)
     */
    int period() default -1;


    /**
     * key的前缀，默认取方法全限定名
     *
     * @return key的前缀
     */
    String prefixKey() default "";

    /**
     * 频控对象，默认el表达指定具体的频控对象
     * 对于ip 和uid模式，需要是http入口的对象，保证RequestHolder里有值
     * @see FrequencyControlTargetEnum
     * @return 对象
     */
    FrequencyControlTargetEnum target() default FrequencyControlTargetEnum.EL;

    /**
     * springEl 表达式，target=EL必填
     *
     * @return 表达式
     */
    String spEl() default "";

    /**
     * 频控时间范围，默认单位秒
     *
     * @return 时间范围
     */
    int time() default -1;

    /**
     * 频控时间单位，默认秒
     *
     * @return 单位
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 单位时间内最大访问次数
     *
     * @return 次数
     */
    int count() default -1;

    long capacity() default -1L; // 令牌桶容量

    double refillRate() default -1.0; // 每秒补充的令牌数

}
