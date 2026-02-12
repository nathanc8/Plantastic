package com.plantastic.backend.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class CorsConfig implements WebMvcConfigurer {
    @Value("${frontend_staging_url}")
    String frontendStagingUrl;
    @Value("${frontend_prod_url}")
    String frontendProdUrl;
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("frontend urls are {} for production and {} for staging", frontendProdUrl, frontendStagingUrl);
        registry.addMapping("/**")
                .allowedOrigins(frontendProdUrl, frontendStagingUrl)
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
