package com.iflove.starter.frequencycontrol.domain.dto;

import com.iflove.starter.frequencycontrol.domain.enums.FrequencyControlStrategyEnum;
import com.iflove.starter.frequencycontrol.domain.enums.FrequencyControlTargetEnum;
import lombok.*;

import java.util.concurrent.TimeUnit;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrequencyControlConfig {
    private FrequencyControlStrategyEnum strategy;
    private int windowSize;
    private int period;
    private String prefixKey;
    private FrequencyControlTargetEnum target;
    private String spEl;
    private int time;
    private TimeUnit unit;
    private int count;
    private long capacity;
    private double refillRate;
}
