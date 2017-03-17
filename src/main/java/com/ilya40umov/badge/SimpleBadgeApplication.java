package com.ilya40umov.badge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * This is the main entry point into the application as well as the main configuration location.
 *
 * @author isorokoumov
 */
@SpringBootApplication
@EnableJpaAuditing
public class SimpleBadgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleBadgeApplication.class, args);
    }
}
