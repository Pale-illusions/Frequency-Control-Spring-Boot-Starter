package com.iflove.starter.frequencycontrol.aspect;

import cn.hutool.core.util.StrUtil;
import com.iflove.starter.frequencycontrol.annotation.FrequencyControl;
import com.iflove.starter.frequencycontrol.config.frequencycontrol.FrequencyControlProperties;
import com.iflove.starter.frequencycontrol.domain.dto.*;
import com.iflove.starter.frequencycontrol.domain.enums.FrequencyControlStrategyEnum;
import com.iflove.starter.frequencycontrol.domain.enums.FrequencyControlTargetEnum;
import com.iflove.starter.frequencycontrol.service.frequencycontrol.FrequencyControlUtil;
import com.iflove.starter.frequencycontrol.utils.SPELUtils;
import com.iflove.starter.frequencycontrol.utils.user.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 频控切面
 */
@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class FrequencyControlAspect {
    private final FrequencyControlProperties properties;

    @Around("@annotation(com.iflove.starter.frequencycontrol.annotation.FrequencyControl)||@annotation(com.iflove.starter.frequencycontrol.annotation.FrequencyControlContainer)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        FrequencyControl[] annotationsByType = method.getAnnotationsByType(FrequencyControl.class);
        // 实现注解配置的动态替换
        List<FrequencyControlConfig> frequencyControlConfigs = injectPropertiesToAnnotations(annotationsByType);

        Map<String, FrequencyControlConfig> keyMap = new HashMap<>();

        for (int i = 0; i < frequencyControlConfigs.size(); i++) {
            FrequencyControlConfig config = frequencyControlConfigs.get(i);
            // 默认为方法限定名 + 注解排名（可能多个）
            String prefix = StrUtil.isBlank(config.getPrefixKey()) ? method.toGenericString() + ":index:" + i : config.getPrefixKey();
            String key = "";
            switch (config.getTarget()) {
                case EL -> key = SPELUtils.parseSPEL(method, joinPoint.getArgs(), config.getSpEl());
                case IP -> key = UserContext.getIp();
                case UID -> key = UserContext.getUserId();
            }
            // 存入keyMap
            keyMap.put(prefix + ":" + key, config);
        }
        // 根据策略分组
        Map<FrequencyControlStrategyEnum, Map<String, FrequencyControlConfig>> configMapByStrategy = keyMap.entrySet().stream()
                .collect(Collectors.groupingBy(
                        entry -> entry.getValue().getStrategy(),
                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
                ));

        Map<String, FrequencyControlConfig> configMap;
        // 固定窗口
        if ((configMap = configMapByStrategy.get(FrequencyControlStrategyEnum.TOTAL_COUNT_WITH_IN_FIX_TIME)) != null) {
            List<FixedWindowDTO> frequencyControlDTOS = configMap.entrySet().stream().map(entrySet -> buildFixedWindowDTO(entrySet.getKey(), entrySet.getValue())).collect(Collectors.toList());
            FrequencyControlUtil.executeWithFrequencyControlList(FrequencyControlStrategyEnum.TOTAL_COUNT_WITH_IN_FIX_TIME, frequencyControlDTOS);
        }
        // 滑动窗口
        if ((configMap = configMapByStrategy.get(FrequencyControlStrategyEnum.SLIDING_WINDOW)) != null) {
            List<SlidingWindowDTO> frequencyControlDTOS = configMap.entrySet().stream().map(entrySet -> buildSlidingWindowDTO(entrySet.getKey(), entrySet.getValue())).collect(Collectors.toList());
            FrequencyControlUtil.executeWithFrequencyControlList(FrequencyControlStrategyEnum.SLIDING_WINDOW, frequencyControlDTOS);
        }
        // 令牌桶
        if ((configMap = configMapByStrategy.get(FrequencyControlStrategyEnum.TOKEN_BUCKET)) != null) {
            List<TokenBucketDTO> frequencyControlDTOS = configMap.entrySet().stream().map(entrySet -> buildTokenBucketDTO(entrySet.getKey(), entrySet.getValue())).collect(Collectors.toList());
            FrequencyControlUtil.executeWithFrequencyControlList(FrequencyControlStrategyEnum.TOKEN_BUCKET, frequencyControlDTOS);
        }
        return joinPoint.proceed();
    }

    /**
     * 将注解参数转换为编程式调用所需要的参数
     *
     * @param key              频率控制Key
     * @param config           频控策略参数
     * @return 编程式调用所需要的参数-FrequencyControlDTO
     */
    private SlidingWindowDTO buildSlidingWindowDTO(String key, FrequencyControlConfig config) {
        SlidingWindowDTO frequencyControlDTO = new SlidingWindowDTO();
        frequencyControlDTO.setWindowSize(config.getWindowSize());
        frequencyControlDTO.setPeriod(config.getPeriod());
        frequencyControlDTO.setCount(config.getCount());
        frequencyControlDTO.setUnit(config.getUnit());
        frequencyControlDTO.setKey(key);
        return frequencyControlDTO;
    }

    /**
     * 将注解参数转换为编程式调用所需要的参数
     *
     * @param key              频率控制Key
     * @param config           频控策略参数
     * @return 编程式调用所需要的参数-FrequencyControlDTO
     */
    private TokenBucketDTO buildTokenBucketDTO(String key, FrequencyControlConfig config) {
        TokenBucketDTO tokenBucketDTO = new TokenBucketDTO(config.getCapacity(), config.getRefillRate());
        tokenBucketDTO.setKey(key);
        return tokenBucketDTO;
    }

    /**
     * 将注解参数转换为编程式调用所需要的参数
     *
     * @param key              频率控制Key
     * @param config           频控策略参数
     * @return 编程式调用所需要的参数-FrequencyControlDTO
     */
    private FixedWindowDTO buildFixedWindowDTO(String key, FrequencyControlConfig config) {
        FixedWindowDTO dto = new FixedWindowDTO();
        dto.setTime(config.getTime());
        dto.setCount(config.getCount());
        dto.setUnit(config.getUnit());
        dto.setKey(key);
        return dto;
    }

    private List<FrequencyControlConfig> injectPropertiesToAnnotations(FrequencyControl[] annotations) {
        return Arrays.stream(annotations)
                .map(annotation -> FrequencyControlConfig.builder()
                        .strategy(!Objects.equals(annotation.strategy(), FrequencyControlStrategyEnum.TOTAL_COUNT_WITH_IN_FIX_TIME)
                                ? annotation.strategy() : properties.getStrategy())
                        .windowSize(!Objects.equals(annotation.windowSize(), -1)
                                ? annotation.windowSize() : properties.getWindowSize())
                        .period(!Objects.equals(annotation.period(), -1)
                                ? annotation.period() : properties.getPeriod())
                        .prefixKey(!StrUtil.isBlank(annotation.prefixKey())
                                ? annotation.prefixKey() : properties.getPrefixKey())
                        .target(!Objects.equals(annotation.target(), FrequencyControlTargetEnum.EL)
                                ? annotation.target() : properties.getTarget())
                        .spEl(!StrUtil.isBlank(annotation.spEl())
                                ? annotation.spEl() : properties.getSpEl())
                        .time(!Objects.equals(annotation.time(), -1)
                                ? annotation.time() : properties.getTime())
                        .unit(!Objects.equals(annotation.unit(), TimeUnit.SECONDS)
                                ? annotation.unit() : properties.getUnit())
                        .count(!Objects.equals(annotation.count(), -1)
                                ? annotation.count() : properties.getCount())
                        .capacity(!Objects.equals(annotation.capacity(), -1L)
                                ? annotation.capacity() : properties.getCapacity())
                        .refillRate(!Objects.equals(annotation.refillRate(), -1.0)
                                ? annotation.refillRate() : properties.getRefillRate())
                        .build()
                ).collect(Collectors.toList());
    }
}
