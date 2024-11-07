package com.iflove.starter.frequencycontrol.domain.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 固定窗口
 */

@Data
public class FixedWindowDTO extends FrequencyControlDTO {
    /**
     * 频控时间范围，默认单位秒
     */
    private Integer time;
}
