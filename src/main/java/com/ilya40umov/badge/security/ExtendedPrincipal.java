package com.ilya40umov.badge.security;

import com.ilya40umov.badge.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

/**
 * Implementation of {@code UserDetails} extended with extra data.
 *
 * @author isorokoumov
 */
public class ExtendedPrincipal extends User {

    private Long accountId;

    public static ExtendedPrincipal fromAccount(Account account) {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(
                account.getAccountPrivileges().stream()
                        .map(ap -> ap.getAccountPrivilegeId().getPrivilege().getAuthorityName())
                        .toArray(String[]::new));
        return new ExtendedPrincipal(account.getAccountId(),
                account.getEmail(), account.getPassword(),
                true, true, true, true,
                authorities);
    }

    private ExtendedPrincipal(Long accountId,
                              String username,
                              String password,
                              boolean enabled,
                              boolean accountNonExpired,
                              boolean credentialsNonExpired,
                              boolean accountNonLocked,
                              Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired,
                accountNonLocked, authorities);
        this.accountId = accountId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

}
