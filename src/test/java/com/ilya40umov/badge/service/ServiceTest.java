package com.ilya40umov.badge.service;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author isorokoumov
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DataJpaTest
@EnableJpaAuditing
@Import(ServiceTest.Config.class)
public @interface ServiceTest {

    @TestConfiguration
    @ComponentScan(value = {"com.ilya40umov.badge.mapper", "com.ilya40umov.badge.service"})
    class Config {

    }

}
