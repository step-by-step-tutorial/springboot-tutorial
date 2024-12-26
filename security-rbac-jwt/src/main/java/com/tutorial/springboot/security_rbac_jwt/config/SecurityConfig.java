package com.tutorial.springboot.security_rbac_jwt.config;

import com.tutorial.springboot.security_rbac_jwt.filter.JwtRequestFilter;
import com.tutorial.springboot.security_rbac_jwt.service.impl.PermissionEvaluatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private SecureResource secureResource;

    private final UserDetailsService userDetailsService;

    private final PermissionEvaluatorService permissionEvaluator;

    private final JwtRequestFilter jwtRequestFilter;

    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserDetailsService userDetailsService, PermissionEvaluatorService permissionEvaluator, JwtRequestFilter jwtRequestFilter, BCryptPasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.permissionEvaluator = permissionEvaluator;
        this.jwtRequestFilter = jwtRequestFilter;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(secureResource.unsecureUrls()).permitAll()
                        .anyRequest().authenticated())
                .formLogin(login -> login.successForwardUrl(secureResource.homeUrl()))
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authenticationProvider(authenticationProvider())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        var handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(permissionEvaluator);
        return handler;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var cors = new CorsConfiguration();
        cors.setAllowedOrigins(List.of(secureResource.corsOriginUrls()));
        cors.setAllowedMethods(List.of(secureResource.corsHttpMethods()));
        cors.setAllowedHeaders(List.of(secureResource.corsHttpHeaders()));

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(secureResource.basePath(), cors);

        return source;
    }
}
