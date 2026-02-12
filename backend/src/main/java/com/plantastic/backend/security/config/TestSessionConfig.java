package com.plantastic.backend.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Profile("test")
public class TestSessionConfig {
  @Bean
  public SessionRepository sessionRepository() {
    return new MapSessionRepository(new ConcurrentHashMap<>());
  }
}