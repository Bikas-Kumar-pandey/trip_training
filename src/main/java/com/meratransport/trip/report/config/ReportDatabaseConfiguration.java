package com.meratransport.trip.report.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

    @Configuration
    @EnableTransactionManagement
    @EnableJpaRepositories(entityManagerFactoryRef = "reportEntityManagerFactory", basePackages = {
            "com.meratransport.trip.report.repository" },transactionManagerRef = "reportTransactionManager")
    @PropertySource("classpath:properties/report/${spring.profiles.active}/datasource.properties")
    public class ReportDatabaseConfiguration {

        @Bean(name = "reportDataSource")
        @ConfigurationProperties(prefix = "report.datasource")
        public DataSource dataSource() {
            return DataSourceBuilder.create().build();
        }

        @Bean(name = "reportEntityManagerFactory")
        public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder,
                                                                               @Qualifier("reportDataSource") DataSource dataSource) {
            return builder.dataSource(dataSource).packages("com.meratransport.trip.report.entity").build();
        }
        @Bean("reportTransactionManager")
        public PlatformTransactionManager tripTransactionManager(@Autowired @Qualifier("reportEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
            return new JpaTransactionManager(entityManagerFactory);
        }
}

