package com.meratransport.trip.configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

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

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:properties/profile/${spring.profiles.active}/datasource.properties")
@EnableJpaRepositories(entityManagerFactoryRef = "profileEntityManagerFactory", basePackages = {
		"com.meratransport.trip.profile.repository" },transactionManagerRef = "profileTransactionManager")
public class ProfileDatabaseConfiguration {
	
	@Bean(name = "profileDataSource")
	@ConfigurationProperties(prefix = "profile.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "profileEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder,
			@Qualifier("profileDataSource") DataSource dataSource) {
		return builder.dataSource(dataSource).packages("com.meratransport.trip.profile.entity").build();

	}
	
	@Bean("profileTransactionManager")
	public PlatformTransactionManager tripTransactionManager(@Autowired @Qualifier("profileEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}	


}
