package com.iflove.starter.frequencycontrol.config.frequencycontrol;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 频率控制自动装配
 */
@EnableAspectJAutoProxy
@EnableConfigurationProperties(FrequencyControlProperties.class)
@ConditionalOnClass(StringRedisTemplate.class)
public class FrequencyControlConfiguration {
}
