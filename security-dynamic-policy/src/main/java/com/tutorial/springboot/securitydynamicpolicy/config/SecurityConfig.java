package com.tutorial.springboot.securitydynamicpolicy.config;

import com.tutorial.springboot.securitydynamicpolicy.filter.PolicyFilter;
import com.tutorial.springboot.securitydynamicpolicy.service.impl.PolicyEngineService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final PolicyEngineService policyEngineService;

    public SecurityConfig(PolicyEngineService policyEngineService) {
        this.policyEngineService = policyEngineService;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var user1 = User.builder()
                .username("user")
                .password(bCryptPasswordEncoder().encode("user"))
                .authorities("USER")
                .build();

        var admin = User.builder()
                .username("admin")
                .password(bCryptPasswordEncoder().encode("admin"))
                .authorities("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user1, admin);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .formLogin(login -> login.successForwardUrl("/api/v1/policies"))
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(new PolicyFilter(policyEngineService), UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

}
