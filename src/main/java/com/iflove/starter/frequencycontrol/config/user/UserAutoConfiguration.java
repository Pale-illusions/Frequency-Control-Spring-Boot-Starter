package com.iflove.starter.frequencycontrol.config.user;

import com.iflove.starter.frequencycontrol.intercepter.UserTransmitFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import static com.iflove.starter.frequencycontrol.domain.constant.FilterOrderConstant.USER_TRANSMIT_FILTER_ORDER;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote 用户上下文自动装配
 */

@ConditionalOnWebApplication
public class UserAutoConfiguration {

    /**
     * 用户信息传递过滤器
     */
    @Bean
    public FilterRegistrationBean<UserTransmitFilter> globalUserTransmitFilter() {
        FilterRegistrationBean<UserTransmitFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new UserTransmitFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(USER_TRANSMIT_FILTER_ORDER);
        return registration;
    }
}
