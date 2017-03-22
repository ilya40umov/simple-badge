package com.ilya40umov.badge.startup;

import com.ilya40umov.badge.entity.Account;
import com.ilya40umov.badge.entity.AccountPrivilege;
import com.ilya40umov.badge.entity.Privilege;
import com.ilya40umov.badge.repository.AccountPrivilegeRepository;
import com.ilya40umov.badge.repository.AccountRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Ensures that at least one admin user is present in the database(useful for the first-time boot).
 *
 * @author isorokoumov
 */
@Component
public class AdminUserCreator implements ApplicationRunner {

    static final String DEFAULT_ADMIN_EMAIL = "admin@simple-badge.com";

    private final AccountRepository accountRepository;
    private final AccountPrivilegeRepository accountPrivilegeRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserCreator(AccountRepository accountRepository,
                            AccountPrivilegeRepository accountPrivilegeRepository,
                            PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.accountPrivilegeRepository = accountPrivilegeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        long countOfAdmins = accountPrivilegeRepository.countByPrivilege(Privilege.ADMINISTER);
        if (countOfAdmins == 0) {
            String encodedPassword = passwordEncoder.encode(DEFAULT_ADMIN_EMAIL);
            accountRepository.save(new Account().setEmail(DEFAULT_ADMIN_EMAIL)
                    .setFirstName("Admin").setLastName("Admin").setPassword(encodedPassword)
                    .addAccountPrivilege(AccountPrivilege.fromPrivilege(Privilege.ADMINISTER)));
        }
    }
}
