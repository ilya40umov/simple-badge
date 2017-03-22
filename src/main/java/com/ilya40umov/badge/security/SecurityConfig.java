package com.ilya40umov.badge.security;

import com.ilya40umov.badge.entity.Account;
import com.ilya40umov.badge.repository.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration
        .EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * Main source of security configuration.
 *
 * @author isorokoumov
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public static UserDetailsService userDetailsService(AccountRepository accountRepository) {
        return username -> {
            if (StringUtils.isEmpty(username)) {
                throw new UsernameNotFoundException("Username must be provided to authenticate.");
            }
            Optional<Account> maybeAccount = accountRepository.findByEmailWithPrivileges(username);
            Account account = maybeAccount.orElseThrow(() ->
                    new UsernameNotFoundException("Account not found: " + username));
            return ExtendedPrincipal.fromAccount(account);
        };
    }

}
