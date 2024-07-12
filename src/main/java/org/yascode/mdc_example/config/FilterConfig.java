package org.yascode.mdc_example.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yascode.mdc_example.filter.MDCFilter;

@Configuration
public class FilterConfig {
    private final MDCFilter mdcFilter;

    public FilterConfig(MDCFilter mdcFilter) {
        this.mdcFilter = mdcFilter;
    }

    @Bean
    public FilterRegistrationBean<MDCFilter> loggingFilter(){
        FilterRegistrationBean<MDCFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(mdcFilter);
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}
