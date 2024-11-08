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
 * @implNote 限流对象枚举
 */
@AllArgsConstructor
@Getter
public enum FrequencyControlTargetEnum {
    UID(1, "UID"),
    IP(2, "IP"),
    EL(3, "EL"),
    ;

    private final Integer code;
    private final String desc;

    private static final Map<Integer, FrequencyControlTargetEnum> cache;

    static {
        cache = Arrays.stream(FrequencyControlTargetEnum.values()).collect(Collectors.toMap(FrequencyControlTargetEnum::getCode, Function.identity()));
    }

    public static FrequencyControlTargetEnum of(Integer code) {
        return cache.get(code);
    }
}
