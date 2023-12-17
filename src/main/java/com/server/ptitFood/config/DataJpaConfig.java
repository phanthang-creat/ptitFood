package com.server.ptitFood.config;

import com.server.ptitFood.config.dsrouting.DataSourceRouter;
import com.server.ptitFood.domain.dto.Username;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.server.ptitFood.domain.repositories",
        entityManagerFactoryRef = "entityManager")
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class DataJpaConfig {


    @Bean
    public AuditorAware<Username> auditor() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(UserDetails.class::cast)
                .map(u -> new Username(u.getUsername()));
    }

    @Bean
    public DataSource dataSource() {
        DataSourceRouter dataSourceRouter = new DataSourceRouter();
        dataSourceRouter.initDatasource(adminAuditor(), staffAuditor());
        return dataSourceRouter;
    }

    @Bean(name = "adminAuditor")
    @ConfigurationProperties(prefix = "spring.admin")
    public DataSource adminAuditor() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "staffAuditor")
    @ConfigurationProperties(prefix = "spring.staff")
    public DataSource staffAuditor() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "customerAuditor")
    @ConfigurationProperties(prefix = "spring.customer")
    public DataSource customerAuditor() {
        return DataSourceBuilder.create().build();
    }

//    Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'entityManager' defined in class path resource [com/server/ptitFood/config/DataJpaConfig.class]: Unsatisfied dependency expressed through method 'entityManagerFactoryBean' parameter 0: No qualifying bean of type 'org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {}
    @Bean(name = "entityManager")
    public LocalContainerEntityManagerFactoryBean entityManager(
            EntityManagerFactoryBuilder builder,
            @Autowired @Qualifier("adminAuditor") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("com.server.ptitFood.domain.entities")
                .persistenceUnit("ptitfood")
                .build();
    }


    @Bean(name = "transactionManager")
    public JpaTransactionManager transactionManager(
            @Autowired @Qualifier("entityManager") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactoryBean.getObject()));
    }

    @Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }
}