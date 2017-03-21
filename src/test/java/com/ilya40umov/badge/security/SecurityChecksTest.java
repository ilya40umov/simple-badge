package com.ilya40umov.badge.security;

import com.ilya40umov.badge.entity.Account;
import com.ilya40umov.badge.entity.AccountPrivilege;
import com.ilya40umov.badge.entity.Privilege;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author isorokoumov
 */
@RunWith(MockitoJUnitRunner.class)
public class SecurityChecksTest {

    private static final Long ID_1 = 1234L;
    private static final Long ID_2 = 4321L;
    private static final String EMAIL = "user@gmail.com";
    private static final String PASSWORD = "password";

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private final SecurityChecks securityChecks = new SecurityChecks();

    @Before
    public void setUpMocks() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void isCurrentAccountOrAdmin_returnsFalse_ifNotAuthenticated() throws Exception {
        when(authentication.isAuthenticated()).thenReturn(false);

        assertThat(securityChecks.isCurrentAccountOrAdmin(ID_1)).isFalse();
    }

    @Test
    public void isCurrentAccountOrAdmin_returnsFalse_ifAnonymousUser() throws Exception {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("anonymousUser");

        assertThat(securityChecks.isCurrentAccountOrAdmin(ID_1)).isFalse();
    }

    @Test
    public void isCurrentAccountOrAdmin_returnsFalse_ifAccountIsNotAdminAndIdsDoNotMatch()
            throws Exception {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(ExtendedPrincipal.fromAccount(new Account()
                .setAccountId(ID_1).setEmail(EMAIL).setPassword(PASSWORD)
                .setAccountPrivileges(Collections.singleton(AccountPrivilege
                        .fromPrivilege(Privilege.CREATE_BADGE)))));

        assertThat(securityChecks.isCurrentAccountOrAdmin(ID_2)).isFalse();
    }

    @Test
    public void isCurrentAccountOrAdmin_returnsTrue_ifAccountIdsMatch()
            throws Exception {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(ExtendedPrincipal.fromAccount(new Account()
                .setAccountId(ID_1).setEmail(EMAIL).setPassword(PASSWORD)
                .setAccountPrivileges(Collections.emptySet())));

        assertThat(securityChecks.isCurrentAccountOrAdmin(ID_1)).isTrue();
    }

    @Test
    public void isCurrentAccountOrAdmin_returnsTrue_ifAccountIsAdmin()
            throws Exception {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(ExtendedPrincipal.fromAccount(new Account()
                .setAccountId(ID_1).setEmail(EMAIL).setPassword(PASSWORD)
                .setAccountPrivileges(Collections.singleton(AccountPrivilege
                        .fromPrivilege(Privilege.ADMINISTER)))));

        assertThat(securityChecks.isCurrentAccountOrAdmin(ID_2)).isTrue();
    }
}
