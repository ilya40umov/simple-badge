package com.ilya40umov.badge.startup;

import com.ilya40umov.badge.entity.Account;
import com.ilya40umov.badge.entity.Privilege;
import com.ilya40umov.badge.repository.AccountPrivilegeRepository;
import com.ilya40umov.badge.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author isorokoumov
 */
@RunWith(MockitoJUnitRunner.class)
public class AdminUserCreatorTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountPrivilegeRepository accountPrivilegeRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private AdminUserCreator adminUserCreator;

    @Before
    public void createCreator() throws Exception {
        adminUserCreator = new AdminUserCreator(accountRepository,
                accountPrivilegeRepository, passwordEncoder);
    }

    @Test
    public void run_doesNotCreateDefaultAdminAccount_whenSomeoneAlreadyHasAdmin() throws Exception {
        when(accountPrivilegeRepository.countByPrivilege(Privilege.ADMINISTER)).thenReturn(1L);

        adminUserCreator.run(new DefaultApplicationArguments(new String[]{}));

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void run_createsDefaultAdminAccount_whenNoOneHasAdmin() throws Exception {
        when(accountPrivilegeRepository.countByPrivilege(Privilege.ADMINISTER)).thenReturn(0L);
        when(passwordEncoder.encode(AdminUserCreator.DEFAULT_ADMIN_EMAIL)).thenReturn("abc012");

        adminUserCreator.run(new DefaultApplicationArguments(new String[]{}));

        verify(accountRepository)
                .save(new Account().setEmail(AdminUserCreator.DEFAULT_ADMIN_EMAIL));
    }
}
