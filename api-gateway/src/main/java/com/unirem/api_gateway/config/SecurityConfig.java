package com.unirem.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception{
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> {})
                .authorizeExchange(exchanges -> exchanges
                        //.pathMatchers("/auth/**", "/gallery/**", "/news/**", "/projects/**",
                                //"/member/**").permitAll()
                        //.pathMatchers("/admin/**").hasAuthority("ADMIN")
                        //.pathMatchers("/member/**").hasAuthority("MEMBER")
                        .anyExchange().permitAll()
                );

        return http.build();
    }
}
