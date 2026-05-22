package com.example.orderapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.orderapp.utils.SnowflakeIdGenerator;

@Configuration
public class SnowflakeConfig {

	@Value("${snowflake.worker-id:1}")
	private Long workerId;

	@Value("${snowflake.datacenter-id:1}")
	private Long datacenterId;

	@Bean
	public SnowflakeIdGenerator snowflakeIdGenerator() {
		return new SnowflakeIdGenerator(workerId, datacenterId);
	}
}