package com.iflove.starter.frequencycontrol.domain.constant;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 限流策略常量
 */

public class FrequencyControlConstant {
    /**
     * 固定窗口
     */
    public static final String TOTAL_COUNT_WITH_IN_FIX_TIME = "TotalCountWithInFixTime";
    /**
     * 滑动窗口
     */
    public static final String SLIDING_WINDOW = "SlidingWindow";
    /**
     * 令牌桶
     */
    public static final String TOKEN_BUCKET = "TokenBucket";
}
