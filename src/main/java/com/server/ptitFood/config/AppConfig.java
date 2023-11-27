package com.server.ptitFood.config;

import com.server.ptitFood.common.helper.encoding.EncodingHelper;
import com.server.ptitFood.config.dsrouting.DataSourceSwitchInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    private final DataSourceSwitchInterceptor dataSourceSwitchInterceptor;

    public AppConfig(DataSourceSwitchInterceptor dataSourceSwitchInterceptor, DataSourceSwitchInterceptor dataSourceSwitchInterceptor1) {
        this.dataSourceSwitchInterceptor = dataSourceSwitchInterceptor1;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

            @Override
            public String encode(CharSequence rawPassword) {
                return EncodingHelper.hashPassword(rawPassword.toString());
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encodedPassword.equals(EncodingHelper.hashPassword(rawPassword.toString()));
            }
        };
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(dataSourceSwitchInterceptor).addPathPatterns("/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }

}
