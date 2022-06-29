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
@PropertySource("classpath:properties/BroadCasting/${spring.profiles.active}/datasource.properties")


@EnableJpaRepositories(entityManagerFactoryRef = "broadCastEntityManagerFactory", basePackages = {
        "com.meratransport.trip.broadCast.repository" },transactionManagerRef = "broadCastTransactionManager")
public class BroadCastConfig {

    @Bean(name = "broadCastDataSource")
    @ConfigurationProperties(prefix = "broadcast.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }



    @Bean(name = "broadCastEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder,
                                                                           @Qualifier("broadCastDataSource") DataSource dataSource) {
        return builder.dataSource(dataSource).packages("com.meratransport.trip.broadCast.entity").build();

    }

    @Bean("broadCastTransactionManager")
    public PlatformTransactionManager tripTransactionManager(@Autowired @Qualifier("broadCastEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }



}