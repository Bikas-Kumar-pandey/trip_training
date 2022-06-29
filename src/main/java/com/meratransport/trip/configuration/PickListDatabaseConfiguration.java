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
@PropertySource("classpath:properties/picklist/${spring.profiles.active}/datasource.properties")
@EnableJpaRepositories(entityManagerFactoryRef = "picklistEntityManagerFactory", basePackages = {
		"com.meratransport.trip.picklist.repository" },transactionManagerRef = "picklistTransactionManager")
public class PickListDatabaseConfiguration {
	
	@Bean(name = "picklistDataSource")
	@ConfigurationProperties(prefix = "picklist.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "picklistEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder,
			@Qualifier("picklistDataSource") DataSource dataSource) {
		return builder.dataSource(dataSource).packages("com.meratransport.trip.picklist.entity").build();

	}
	
	@Bean("picklistTransactionManager")
	public PlatformTransactionManager tripTransactionManager(@Autowired @Qualifier("picklistEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}	

}
