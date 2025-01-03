package com.tutorial.springboot.securitydynamicpolicy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.envers.repository.config.EnableEnversRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableEnversRepositories(basePackages = "com.tutorial.springboot.securitydynamicpolicy.repository")
@Configuration
public class AuditConfig {

}
