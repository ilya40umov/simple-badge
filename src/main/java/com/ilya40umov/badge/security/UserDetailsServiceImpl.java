package com.ilya40umov.badge.security;

import com.ilya40umov.badge.entity.Account;
import com.ilya40umov.badge.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * Lookups user details by a provided username.
 *
 * @author isorokoumov
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    public UserDetailsServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("Username must be provided to authenticate.");
        }
        Optional<Account> maybeAccount = accountRepository.findByEmailWithPrivileges(username);
        Account account = maybeAccount.orElseThrow(() ->
                new UsernameNotFoundException("Account not found: " + username));
        return ExtendedPrincipal.fromAccount(account);
    }
}
