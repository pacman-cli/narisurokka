package org.example.apigateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {

    /**
     * Resolves the key to rate limit on. By default, we use the client's IP address.
     * In a production environment using proxies/load balancers, check X-Forwarded-For headers.
     */
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> {
            String ip = exchange.getRequest().getRemoteAddress() != null
                    ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
                    : "unknown";

            // Allow checking X-Forwarded-For if available
            String forwardedFor = exchange.getRequest().getHeaders().getFirst(HttpHeaders.X_FORWARDED_FOR);
            if (forwardedFor != null && !forwardedFor.isEmpty()) {
                ip = forwardedFor.split(",")[0].trim();
            }

            return Mono.just(ip);
        };
    }
}

