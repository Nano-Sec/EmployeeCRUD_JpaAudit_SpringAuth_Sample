package com.restcrud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.restcrud.entities.Employee;
import com.restcrud.utils.AuditorAwareImpl;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaConfig {
	@Bean
    public AuditorAware<Employee> auditorAware() {
        return new AuditorAwareImpl();
	}
}
