package com.ilya40umov.badge.rest.v1;

import com.ilya40umov.badge.entity.Account;
import com.ilya40umov.badge.entity.AccountPrivilege;
import com.ilya40umov.badge.entity.Privilege;
import com.ilya40umov.badge.security.ExtendedPrincipal;
import com.ilya40umov.badge.security.SecurityChecks;
import com.ilya40umov.badge.service.AccountService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.config.annotation.authentication.builders
        .AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration
        .EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration
        .WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;

/**
 * @author isorokoumov
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@WebMvcTest
@Import(ControllerTest.Config.class)
public @interface ControllerTest {

    Long ACCOUNT_1_ID = 1L;
    String ACCOUNT_1_LOGIN = "acc1@gmail.com";

    Long ACCOUNT_2_ID = 2L;
    String ACCOUNT_2_LOGIN = "acc2@gmail.com";

    Long ADMIN_ID = 777L;
    String ADMIN_LOGIN = "admin@gmail.com";

    @TestConfiguration
    @EnableWebSecurity
    @EnableSpringDataWebSupport
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @ComponentScan("com.ilya40umov.badge.rest.v1")
    class Config extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // all requests are permitted by default to let annotations take care of everything
            http.authorizeRequests().anyRequest().permitAll();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService());
        }

        @Bean
        public UserDetailsService userDetailsService() {
            ExtendedPrincipal simpleUserPrincipal = ExtendedPrincipal.fromAccount(new Account()
                    .setAccountId(ACCOUNT_1_ID)
                    .setEmail(ACCOUNT_1_LOGIN)
                    .setPassword(ACCOUNT_1_LOGIN)
                    .setAccountPrivileges(Collections.emptySet()));
            ExtendedPrincipal simpleUser2Principal = ExtendedPrincipal.fromAccount(new Account()
                    .setAccountId(ACCOUNT_2_ID)
                    .setEmail(ACCOUNT_2_LOGIN)
                    .setPassword(ACCOUNT_2_LOGIN)
                    .setAccountPrivileges(Collections.emptySet()));

            ExtendedPrincipal adminUserPrincipal = ExtendedPrincipal.fromAccount(new Account()
                    .setAccountId(ADMIN_ID)
                    .setEmail(ADMIN_LOGIN)
                    .setPassword(ADMIN_LOGIN)
                    .setAccountPrivileges(Collections.singleton(
                            AccountPrivilege.fromPrivilege(Privilege.ADMINISTER))));

            Map<String, ExtendedPrincipal> principalByUsername = new HashMap<>();
            principalByUsername.put(simpleUserPrincipal.getUsername(), simpleUserPrincipal);
            principalByUsername.put(simpleUser2Principal.getUsername(), simpleUser2Principal);
            principalByUsername.put(adminUserPrincipal.getUsername(), adminUserPrincipal);

            return username -> {
                if (!principalByUsername.containsKey(username)) {
                    throw new UsernameNotFoundException("Not found: " + username);
                }
                return principalByUsername.get(username);
            };
        }

        @Bean
        public SecurityChecks securityChecks() {
            return new SecurityChecks();
        }

        @Bean
        public AccountService accountService() {
            return mock(AccountService.class);
        }

    }

}
