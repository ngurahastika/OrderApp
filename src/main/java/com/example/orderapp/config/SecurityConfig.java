package com.example.orderapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Setter
@Getter
public class SecurityConfig {

	@Value("${security.cors.allowedOrigin}")
	private String allowedOrigin;

	@Value("${security.cors.allowedMethod}")
	private String[] allowedMethod;

}