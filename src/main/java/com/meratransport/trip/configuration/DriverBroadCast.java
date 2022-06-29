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
@PropertySource("classpath:properties/DriverBroadCast/${spring.profiles.active}/datasource.properties")


@EnableJpaRepositories(entityManagerFactoryRef = "driverbroadcastEntityManagerFactory", basePackages = {
        "com.meratransport.trip.driverbroadCast.repository" },transactionManagerRef = "driverbroadCastTransactionManager")
public class DriverBroadCast {

    @Bean(name = "driverbroadCastDataSource")
    @ConfigurationProperties(prefix = "driverbroadcast.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }



    @Bean(name = "driverbroadcastEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder,
                                                                           @Qualifier("driverbroadCastDataSource") DataSource dataSource) {
        return builder.dataSource(dataSource).packages("com.meratransport.trip.driverbroadCast.entity").build();

    }


    @Bean("driverbroadCastTransactionManager")
    public PlatformTransactionManager tripTransactionManager(@Autowired @Qualifier("driverbroadcastEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }



}