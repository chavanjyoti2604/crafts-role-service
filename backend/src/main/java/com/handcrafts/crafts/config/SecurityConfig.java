package com.handcrafts.crafts.config;

import com.handcrafts.crafts.filter.JwtAuthFilter;
import com.handcrafts.crafts.security.RoleBasedUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final RoleBasedUserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, RoleBasedUserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        // ✅ Public routes
                        .requestMatchers(
                                "/user/register", "/user/login",
                                "/seller/register", "/seller/login",
                                "/admin/register", "/admin/login",
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"
                        ).permitAll()

                        // ✅ Role-based routes using enums
                        .requestMatchers("/user/**").hasAuthority(RoleEnum.USER.getAuthority())
                        .requestMatchers("/seller/**").hasAuthority(RoleEnum.SELLER.getAuthority())
                        .requestMatchers("/admin/**").hasAuthority(RoleEnum.ADMIN.getAuthority())

                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(
                List.of(HttpMethodEnum.GET, HttpMethodEnum.POST, HttpMethodEnum.PUT,
                                HttpMethodEnum.DELETE, HttpMethodEnum.OPTIONS)
                        .stream().map(Enum::name).collect(Collectors.toList())
        );
        config.setAllowedHeaders(
                List.of(HttpHeaderEnum.AUTHORIZATION, HttpHeaderEnum.CACHE_CONTROL, HttpHeaderEnum.CONTENT_TYPE)
                        .stream().map(HttpHeaderEnum::getValue).collect(Collectors.toList())
        );
        config.setExposedHeaders(
                List.of(HttpHeaderEnum.AUTHORIZATION.getValue())
        );

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
