package com.plantastic.backend.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class CookieConfig {
  
  @Value("${spring.profiles.active:local}") 
  private String activeProfile;

  @Bean
  public DefaultCookieSerializer cookieSerializer() {
    DefaultCookieSerializer serializer = new DefaultCookieSerializer();

    // Déterminer si on est en prod
    boolean isProd = "prod".equals(activeProfile);

    // Config du cookie
    serializer.setSameSite(isProd ? "None" : "Lax");
    serializer.setUseSecureCookie(isProd); // HTTPS seulement en prod
    serializer.setUseHttpOnlyCookie(true);
    serializer.setCookiePath("/");

    return serializer;
  }
}