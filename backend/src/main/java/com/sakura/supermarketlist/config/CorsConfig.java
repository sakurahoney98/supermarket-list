package com.sakura.supermarketlist.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
	
	@Value("${app.cors.mappings}")
	private String allowedMappings;
	
	@Value("${app.cors.origins}")
	private String allowedOrigins;
	
	@Value("${app.cors.methods}")
	private String allowedMethods;
	
	@Value("${app.cors.headers}")
	private String allowedHeaders;

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				
				registry.addMapping(allowedMappings)
				.allowedOriginPatterns(allowedOrigins)
				.allowedMethods(allowedMethods)
				.allowedHeaders(allowedHeaders);
			}
		};
	}
}
