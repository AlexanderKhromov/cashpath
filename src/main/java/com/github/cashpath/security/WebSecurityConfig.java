package com.github.cashpath.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Log4j2
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Value("${spring.security.user.name}")
    private String userName;
    @Value("${spring.security.user.password}")
    private String userPassword;

    /**
     * DEV-profile: CSRF disabled, all requests are allowed,
     * for simplifying developing.
     */
    @Bean
    @Profile("dev")
    public SecurityFilterChain devSecurity(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .headers(headers -> headers
                        .frameOptions(Customizer.withDefaults()).disable()); // disabling complaining of H2/Thymeleaf

        log.info("⚙️ DEV security mode: CSRF disabled, all requests permitted.");
        return http.build();
    }

    // 🔐 PROD — with CSRF and Basic Auth
    @Bean
    @Profile("prod")
    public SecurityFilterChain prodSecurity(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").authenticated() //protecting API
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults());// Basic Auth for API

        log.info("🔒 PROD security mode: CSRF enabled, authentication required.");
        return http.build();
    }

    @Bean
    @Profile("prod")
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.withUsername(userName)
                .password(passwordEncoder.encode(userPassword)) // hashed password
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

