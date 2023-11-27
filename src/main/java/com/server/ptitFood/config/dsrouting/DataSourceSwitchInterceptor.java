package com.server.ptitFood.config.dsrouting;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.sql.DataSource;

@Component
public class DataSourceSwitchInterceptor implements HandlerInterceptor {
    private final DataSource adminDataSource;
    private final DataSource userDataSource;

    @Autowired
    public DataSourceSwitchInterceptor(@Qualifier("adminAuditor") DataSource adminDataSource,
                                       @Qualifier("customerAuditor") DataSource userDataSource) {
        this.adminDataSource = adminDataSource;
        this.userDataSource = userDataSource;
    }

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                DataSourceContextHolder.setDataSource(adminDataSource); // Set adminDataSource for admin requests
            } else {
                DataSourceContextHolder.setDataSource(userDataSource); // Set userDataSource for other requests
            }
        }
        return true;
    }
}
