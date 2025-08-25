package com.unirem.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf().disable()
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/auth/**", "/gallery/**", "/news/**", "/projects/**",
                                "/member/**").permitAll()
                        .pathMatchers("/admin/**").hasAuthority("ADMIN")
                        //.pathMatchers("/member/**").hasAuthority("MEMBER")
                        .anyExchange().permitAll()
                );
        return http.build();
    }
}
