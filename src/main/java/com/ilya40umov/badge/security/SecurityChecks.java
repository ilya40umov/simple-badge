package com.ilya40umov.badge.security;

import com.ilya40umov.badge.entity.Privilege;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Provides additional security checks that can be used either directly or in security EL
 * (e.g. in {@link org.springframework.security.access.prepost.PreAuthorize} annotation).
 *
 * @author isorokoumov
 */
@Component
public class SecurityChecks {

    /**
     * Returns {@code true} if the provided {@code accountId} matches the one of the currently
     * authenticated account or the current account has administrative privilege.
     */
    public boolean isCurrentAccountOrAdmin(Long accountId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof ExtendedPrincipal)) {
            return false;
        }
        ExtendedPrincipal extendedPrincipal = (ExtendedPrincipal) authentication.getPrincipal();
        boolean isAdmin = extendedPrincipal.getAuthorities().stream()
                .anyMatch(ga -> ga.getAuthority().equals(Privilege.ADMINISTER.getAuthorityName()));
        return isAdmin || (extendedPrincipal.getAccountId() != null
                && extendedPrincipal.getAccountId().equals(accountId));
    }

}
