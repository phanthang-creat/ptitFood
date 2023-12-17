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
        System.out.println("DataSourceSwitchInterceptor" + request.getRequestURI());
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
            if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                System.out.println("admin");
                DataSourceContextHolder.setDataSource(adminDataSource); // Set adminDataSource for admin requests
            } else {
                DataSourceContextHolder.setDataSource(userDataSource); // Set userDataSource for other requests
            }
        }
        return true;
    }
}
