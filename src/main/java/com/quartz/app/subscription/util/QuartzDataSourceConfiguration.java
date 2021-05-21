package com.quartz.app.subscription.util;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class QuartzDataSourceConfiguration {

	@Bean
	@QuartzDataSource
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource quartzDataSource() {
		return DataSourceBuilder.create().build();
	}
}
