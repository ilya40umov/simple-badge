package com.ilya40umov.badge.repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main source of JPA-related configuration.
 *
 * @author isorokoumov
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
