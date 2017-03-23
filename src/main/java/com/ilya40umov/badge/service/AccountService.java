package com.ilya40umov.badge.service;

import com.ilya40umov.badge.dto.AccountDto;
import com.ilya40umov.badge.dto.command.CreateAccount;
import com.ilya40umov.badge.dto.partial.AccountDetails;
import com.ilya40umov.badge.dto.partial.PasswordDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Provides operations on accounts.
 *
 * @author isorokoumov
 */
public interface AccountService {

    Optional<AccountDto> findById(Long accountId);

    boolean isEmailRegistered(String email);

    AccountDto createAccount(CreateAccount createAccount);

    void updateAccount(Long accountId, AccountDetails accountDetails);

    void updatePassword(Long accountId, PasswordDetails passwordDetails);

    void deleteAccount(Long accountId);

    Page<AccountDto> listWithBadgeId(Long badgeId, Pageable pageable);

    void addPrivilege(Long accountId, Integer privilegeId);

    void removePrivilege(Long accountId, Integer privilegeId);

}
