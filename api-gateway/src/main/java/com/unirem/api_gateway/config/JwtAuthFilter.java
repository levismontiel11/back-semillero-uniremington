package com.unirem.api_gateway.config;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.http.HttpHeaders;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {
    @Value("${jwt.secret}")
    private String secretKey;

    public JwtAuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = null;
            String path = exchange.getRequest().getURI().getPath();
            
            if (path.startsWith("/gallery") ||
            path.startsWith("/news/") || path.startsWith("/projects")
            || path.startsWith("/member")) {
                return chain.filter(exchange);
            }

            if (exchange.getRequest().getCookies().containsKey("jwt")) {
                token = exchange.getRequest().getCookies().getFirst("jwt").getValue();
            }
            else {
                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7);
                }
            }

            if (token == null || token.isEmpty()) {
                return this.onError(exchange, "JWT token is missing", HttpStatus.UNAUTHORIZED);
            }

            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(secretKey.getBytes())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                
                if (path.startsWith("/admin/")) {
                    String role = claims.get("role", String.class);
                    if (!"ADMIN".equals(role)) {
                        return this.onError(exchange, "Admin role required", HttpStatus.FORBIDDEN);
                    }
                }
                /*else if (path.startsWith("/member/")){
                    String role = claims.get("role", String.class);
                    if (!"MEMBER".equals(role)) {
                        return this.onError(exchange, "Member role required", HttpStatus.FORBIDDEN);
                    }
                }*/

                exchange.getRequest().mutate()
                        .header("userId", String.valueOf(claims.get("userId", Long.class)))
                        .header("role", claims.get("role", String.class))
                        .build();

            } catch (Exception e) {
                return this.onError(exchange, "Invalid JWT token: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {
        
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
    exchange.getResponse().setStatusCode(httpStatus);
    byte[] bytes = err.getBytes(StandardCharsets.UTF_8);
    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
    return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
