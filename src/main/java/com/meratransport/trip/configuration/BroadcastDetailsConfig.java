package com.meratransport.trip.configuration;

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
@PropertySource("classpath:properties/BroadcastDetails/${spring.profiles.active}/datasource.properties")

@EnableJpaRepositories(entityManagerFactoryRef = "broadcastDetailsEntityManagerFactory", basePackages = {
        "com.meratransport.trip.broadcastDetail.broadcastDetailsRepo" },transactionManagerRef = "broadcastDetailsTransactionManager")
public class BroadcastDetailsConfig {

    @Bean(name = "broadcastDetailsDataSource")
    @ConfigurationProperties(prefix = "broadcastdetails.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }



    @Bean(name = "broadcastDetailsEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder,
                                                                           @Qualifier("broadcastDetailsDataSource") DataSource dataSource) {
        return builder.dataSource(dataSource).packages("com.meratransport.trip.broadcastDetail.broadcastDetailEntity").build();

    }

    @Bean("broadcastDetailsTransactionManager")
    public PlatformTransactionManager tripTransactionManager(@Autowired @Qualifier("broadcastDetailsEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }



}