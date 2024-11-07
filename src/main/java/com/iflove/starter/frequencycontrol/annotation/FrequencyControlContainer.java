package com.iflove.starter.frequencycontrol.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 可重复注解容器
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FrequencyControlContainer {
    // 可重复注解
    FrequencyControl[] value();
}
