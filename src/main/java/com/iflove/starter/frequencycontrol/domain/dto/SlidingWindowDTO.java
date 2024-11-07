package com.iflove.starter.frequencycontrol.domain.dto;

import lombok.Data;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 滑动窗口
 */

@Data
public class SlidingWindowDTO extends FrequencyControlDTO {

    /**
     * 窗口大小，默认 10 s
     */
    private int windowSize;

    /**
     * 窗口最小周期 1s (窗口大小是 10s， 1s一个小格子，-共10个格子)
     */
    private int period;
}
