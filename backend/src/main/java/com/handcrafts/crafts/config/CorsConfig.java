package com.handcrafts.crafts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.*;
import org.springframework.web.filter.CorsFilter;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class CorsConfig {

    @Bean(name = "customCorsFilter")
    public CorsFilter customCorsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:3000"));

        config.setAllowedMethods(
                List.of(HttpMethodEnum.GET, HttpMethodEnum.POST, HttpMethodEnum.PUT, HttpMethodEnum.DELETE, HttpMethodEnum.OPTIONS)
                        .stream().map(Enum::name).collect(Collectors.toList())
        );

        config.setAllowedHeaders(
                List.of(HttpHeaderEnum.AUTHORIZATION, HttpHeaderEnum.CONTENT_TYPE)
                        .stream().map(HttpHeaderEnum::getValue).collect(Collectors.toList())
        );

        config.setExposedHeaders(
                List.of(HttpHeaderEnum.AUTHORIZATION.getValue())
        );

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
