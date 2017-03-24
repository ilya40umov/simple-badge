package com.ilya40umov.badge.security;

import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders
        .AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration
        .EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration
        .WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Main source of security configuration.
 *
 * @author isorokoumov
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Configuration
    @Order(1)
    public static class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

        private final UserDetailsService userDetailsService;
        private final NoRedirectAuthSuccessHandler noRedirectAuthSuccessHandler;

        public ApiSecurityConfig(UserDetailsService userDetailsService,
                                 NoRedirectAuthSuccessHandler noRedirectAuthSuccessHandler) {
            this.userDetailsService = userDetailsService;
            this.noRedirectAuthSuccessHandler = noRedirectAuthSuccessHandler;
        }

        @Override
        protected void configure(AuthenticationManagerBuilder authManager) throws Exception {
            authManager
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder());
        }

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            // configuring security only for API endpoints
            httpSecurity.antMatcher("/api/**")
                    // allows to accept login credentials as json
                    .addFilterAt(jsonAcceptingAuthFilter(),
                            UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    // XXX all requests are permitted to let annotations decide on access
                    .anyRequest().permitAll()
                    .and()
                    .logout()
                    .logoutUrl("/api/logout")
                    // TODO success/failure handlers for logout
                    .and()
                    .exceptionHandling()
                    // 401 UNAUTHORIZED for requests without a session
                    .authenticationEntryPoint(new Http401AuthenticationEntryPoint("/api/login"))
                    .and()
                    .csrf();
        }

        @Bean
        public JsonAcceptingAuthFilter jsonAcceptingAuthFilter() throws Exception {
            JsonAcceptingAuthFilter authFilter = new JsonAcceptingAuthFilter();
            authFilter.setRequiresAuthenticationRequestMatcher(
                    new AntPathRequestMatcher("/api/login", "POST"));
            authFilter.setAuthenticationManager(authenticationManagerBean());
            authFilter.setAuthenticationSuccessHandler(noRedirectAuthSuccessHandler);
            authFilter.setAuthenticationFailureHandler(noRedirectAuthFailureHandler());
            authFilter.setUsernameParameter("username");
            authFilter.setPasswordParameter("password");
            return authFilter;
        }

        @Bean
        protected SimpleUrlAuthenticationFailureHandler noRedirectAuthFailureHandler() {
            return new SimpleUrlAuthenticationFailureHandler();
        }

    }

    /**
     * Security configuration for non-API requests.
     */
    @Configuration
    @Order(2)
    public static class DefaultSecurityConfig extends WebSecurityConfigurerAdapter {

        private final UserDetailsService userDetailsService;

        public DefaultSecurityConfig(UserDetailsService userDetailsService) {
            this.userDetailsService = userDetailsService;
        }

        @Override
        protected void configure(AuthenticationManagerBuilder authManager) throws Exception {
            authManager
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder());
        }

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                    .authorizeRequests()
                    .antMatchers("/css/**").permitAll()
                    .anyRequest().authenticated()
                    .anyRequest().hasRole("ADMIN")
                    .and()
                    .formLogin()
                    .loginPage("/login").permitAll()
                    .and()
                    .logout()
                    .logoutUrl("/logout").permitAll()
                    .and()
                    .csrf();
        }

    }

}
