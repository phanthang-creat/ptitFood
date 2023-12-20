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

    private final DataSource staffDataSource;

    @Autowired
    public DataSourceSwitchInterceptor(@Qualifier("adminAuditor") DataSource adminDataSource,
                                        @Qualifier("staffAuditor") DataSource staffDataSource,
                                       @Qualifier("customerAuditor") DataSource userDataSource) {
        this.adminDataSource = adminDataSource;
        this.userDataSource = userDataSource;
        this.staffDataSource = staffDataSource;
    }

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        System.out.println("DataSourceSwitchInterceptor" + request.getRequestURI());
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                System.out.println("admin");
                DataSourceContextHolder.setDataSource(adminDataSource); // Set adminDataSource for admin requests
            } else {
                if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STAFF"))) {
                    System.out.println("staff");
                    DataSourceContextHolder.setDataSource(staffDataSource); // Set userDataSource for staff requests
                } else {
                    System.out.println("user");
                    DataSourceContextHolder.setDataSource(userDataSource); // Set userDataSource for user requests
                }
            }
        }
        return true;
    }
}
