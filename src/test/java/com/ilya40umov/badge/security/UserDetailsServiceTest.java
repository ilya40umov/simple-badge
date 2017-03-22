package com.ilya40umov.badge.security;

import com.ilya40umov.badge.entity.Account;
import com.ilya40umov.badge.entity.AccountPrivilege;
import com.ilya40umov.badge.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.ilya40umov.badge.entity.Privilege.CREATE_BADGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * @author isorokoumov
 */
@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceTest {

    private static final String EMAIL = "email";

    @Mock
    private AccountRepository accountRepository;

    private UserDetailsService userDetailsService;

    @Before
    public void setUp() throws Exception {
        userDetailsService = SecurityConfig.userDetailsService(accountRepository);
    }

    @Test
    public void loadUserByUsername_throwsException_ifAccountNotFound() throws Exception {
        when(accountRepository.findByEmailWithPrivileges(EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(EMAIL))
                .isInstanceOf(UsernameNotFoundException.class);

    }

    @Test
    public void loadUserByUsername_returnsUserWithAuthorities_ifAccountFound() throws Exception {
        when(accountRepository.findByEmailWithPrivileges(EMAIL)).thenReturn(Optional.of(
                new Account().setAccountId(123L).setEmail(EMAIL).setPassword("password")
                        .addAccountPrivilege(AccountPrivilege.fromPrivilege(CREATE_BADGE))));

        UserDetails user = userDetailsService.loadUserByUsername(EMAIL);

        assertThat(user.getUsername()).isEqualTo(EMAIL);
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getAuthorities()).hasSize(1);
        assertThat(user.getAuthorities().iterator().next().getAuthority())
                .isEqualTo(CREATE_BADGE.getAuthorityName());
    }
}
