package com.plantastic.backend.security.config;

import com.plantastic.backend.security.handler.CustomLoginFailureHandler;
import com.plantastic.backend.security.handler.CustomLoginSuccessHandler;
import com.plantastic.backend.security.handler.CustomLogoutSuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableSpringHttpSession
public class SecurityConfigurer {

    private final CustomLoginSuccessHandler customLoginSuccessHandler;
    private final CustomLoginFailureHandler customLoginFailureHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    private static final String LOGIN_ROUTE = "/api/auth/login";
    private static final String REGISTER_ROUTE = "/api/auth/register";
    private static final String LOGOUT_ROUTE = "/api/auth/logout";
    private static final String ADD_PLANT_ROUTE = "/api/plant/add-one";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, Environment env) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/", LOGIN_ROUTE, REGISTER_ROUTE).permitAll()
                    .requestMatchers(ADD_PLANT_ROUTE).hasRole("ADMIN")
                    .anyRequest().authenticated()
                )
            // Exception handling : return 401 Unauthorized instead of 200 with default HTML login form if user is not authenticated
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\":\"User is not authenticated\"}");
                        })
                );

        //Login with cookie session
        http.formLogin(form -> form
                .loginProcessingUrl(LOGIN_ROUTE)
                //Login success handler : sends a 200 http code and a json
                .successHandler(customLoginSuccessHandler)
                //Login failure handler : sends a 401 http code and a json
                .failureHandler(customLoginFailureHandler)
        );
        //Logout with cookie session : delete cookies on logout
        http.logout(logout -> logout
                .logoutUrl(LOGOUT_ROUTE)
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessHandler(customLogoutSuccessHandler)
        );
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
