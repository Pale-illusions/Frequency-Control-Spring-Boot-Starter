package com.iflove.starter.frequencycontrol.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 限流策略枚举
 */
@AllArgsConstructor
@Getter
public enum FrequencyControlStrategyEnum {
    TOTAL_COUNT_WITH_IN_FIX_TIME(1, "固定窗口"),
    SLIDING_WINDOW(2, "滑动窗口"),
    TOKEN_BUCKET(3, "令牌桶"),
    ;

    private final Integer code;
    private final String desc;

    private static final Map<Integer, FrequencyControlStrategyEnum> cache;

    static {
        cache = Arrays.stream(FrequencyControlStrategyEnum.values()).collect(Collectors.toMap(FrequencyControlStrategyEnum::getCode, Function.identity()));
    }

    public static FrequencyControlStrategyEnum of(Integer code) {
        return cache.get(code);
    }
}
