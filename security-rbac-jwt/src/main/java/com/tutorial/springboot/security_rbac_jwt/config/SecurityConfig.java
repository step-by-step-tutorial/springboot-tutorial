package com.tutorial.springboot.security_rbac_jwt.config;

import com.tutorial.springboot.security_rbac_jwt.dto.SecureProperties;
import com.tutorial.springboot.security_rbac_jwt.filter.DynamicPolicyFilter;
import com.tutorial.springboot.security_rbac_jwt.filter.JwtRequestFilter;
import com.tutorial.springboot.security_rbac_jwt.service.PolicyEngine;
import com.tutorial.springboot.security_rbac_jwt.service.impl.PermissionEvaluatorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final PermissionEvaluatorService permissionEvaluator;

    private final SecureProperties secureProperties;

    private final JwtRequestFilter jwtRequestFilter;

    private final PolicyEngine policyEngine;

    public SecurityConfig(
            UserDetailsService userDetailsService,
            BCryptPasswordEncoder passwordEncoder,
            PermissionEvaluatorService permissionEvaluator,
            SecureProperties secureProperties,
            JwtRequestFilter jwtRequestFilter,
            PolicyEngine policyEngine
    ) {
        this.userDetailsService = userDetailsService;
        this.permissionEvaluator = permissionEvaluator;
        this.jwtRequestFilter = jwtRequestFilter;
        this.passwordEncoder = passwordEncoder;
        this.secureProperties = secureProperties;
        this.policyEngine = policyEngine;
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
                        .requestMatchers(secureProperties.unsecureUrls()).permitAll()
                        .anyRequest().authenticated())
                .formLogin(login -> login.successForwardUrl(secureProperties.homeUrl()))
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authenticationProvider(authenticationProvider())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(new DynamicPolicyFilter(policyEngine), UsernamePasswordAuthenticationFilter.class)
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
        cors.setAllowedOrigins(List.of(secureProperties.corsOriginUrls()));
        cors.setAllowedMethods(List.of(secureProperties.corsHttpMethods()));
        cors.setAllowedHeaders(List.of(secureProperties.corsHttpHeaders()));

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(secureProperties.basePath(), cors);

        return source;
    }
}
