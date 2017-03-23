
package com.ilya40umov.badge.service;

import com.ilya40umov.badge.dto.AccountDto;
import com.ilya40umov.badge.dto.command.CreateAccount;
import com.ilya40umov.badge.dto.partial.AccountDetails;
import com.ilya40umov.badge.dto.partial.PasswordDetails;
import com.ilya40umov.badge.entity.Account;
import com.ilya40umov.badge.entity.AccountPrivilege;
import com.ilya40umov.badge.entity.Privilege;
import com.ilya40umov.badge.mapper.AccountMapper;
import com.ilya40umov.badge.repository.AccountRepository;
import com.ilya40umov.badge.service.exception.EntityNotFoundException;
import com.ilya40umov.badge.service.exception.InvalidRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author isorokoumov
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private static final String ACCOUNT_NOT_FOUND_MESSAGE =
            "Account with accountId:'%s' not found in DB.";

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository,
                              AccountMapper accountMapper,
                              PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<AccountDto> findById(Long accountId) {
        return accountRepository.findByIdWithAllJoins(accountId).map(accountMapper::toAccountDto);
    }

    @Override
    public boolean isEmailRegistered(String email) {
        // XXX consider adding a custom query to repository to handle this request
        return accountRepository.findByEmail(email).isPresent();
    }

    @Override
    public AccountDto createAccount(CreateAccount createAccount) {
        // XXX consider doing isEmailRegistered() check first
        String encodedPassword = passwordEncoder.encode(
                createAccount.getPassword().getNewPassword());
        Account account = new Account()
                .setEmail(createAccount.getAccount().getEmail())
                .setFirstName(createAccount.getAccount().getFirstName())
                .setLastName(createAccount.getAccount().getLastName())
                .setPassword(encodedPassword);
        return accountMapper.toAccountDto(accountRepository.save(account));
    }

    @Override
    public void updateAccount(Long accountId, AccountDetails accountDetails) {
        Account account = ensureAccountNotNull(accountRepository::findOne, accountId);
        account.setEmail(accountDetails.getEmail());
        account.setFirstName(accountDetails.getFirstName());
        account.setLastName(accountDetails.getLastName());
        accountRepository.save(account);
    }

    @Override
    public void updatePassword(Long accountId, PasswordDetails passwordDetails) {
        Account account = ensureAccountNotNull(accountRepository::findOne, accountId);
        if (!passwordEncoder.matches(passwordDetails.getCurrentPassword(), account.getPassword())) {
            throw new InvalidRequestException("Current password does not match");
        }
        account.setPassword(passwordEncoder.encode(passwordDetails.getNewPassword()));
        accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Long accountId) {
        Account account = ensureAccountNotNull(accountRepository::findOne, accountId);
        accountRepository.delete(account);
    }

    @Override
    public Page<AccountDto> listWithBadgeId(Long badgeId, Pageable pageable) {
        return accountRepository.listWithBadgeId(badgeId, pageable)
                .map(accountMapper::toAccountDto);
    }

    @Override
    public void addPrivilege(Long accountId, Integer privilegeId) {
        Privilege privilege = Privilege.fromId(privilegeId).orElseThrow(
                () -> new InvalidRequestException("No privilege with ID: " + privilegeId));
        Account account =
                ensureAccountPresent(accountRepository::findByIdWithPrivileges, accountId);
        boolean alreadyGranted = account.getAccountPrivileges().stream()
                .anyMatch(ap -> ap.getAccountPrivilegeId().getPrivilege().equals(privilege));
        if (alreadyGranted) {
            throw new InvalidRequestException(String.format(
                    "User already has the privilege (privilegeId: %s)", privilegeId));
        }
        account.addAccountPrivilege(AccountPrivilege.fromPrivilege(privilege));
        accountRepository.save(account);
    }

    @Override
    public void removePrivilege(Long accountId, Integer privilegeId) {
        Privilege privilege = Privilege.fromId(privilegeId).orElseThrow(
                () -> new InvalidRequestException("No privilege with ID: " + privilegeId));
        Account account =
                ensureAccountPresent(accountRepository::findByIdWithPrivileges, accountId);
        Optional<AccountPrivilege> accountPrivilege = account.getAccountPrivileges().stream()
                .filter(ap -> ap.getAccountPrivilegeId().getPrivilege().equals(privilege))
                .findAny();
        if (!accountPrivilege.isPresent()) {
            throw new InvalidRequestException(String.format(
                    "User does not have the privilege (privilegeId: %s)", privilegeId));
        }
        account.removeAccountPrivilege(accountPrivilege.get());
        accountRepository.save(account);
    }

    private Account ensureAccountNotNull(Function<Long, Account> findAccountById, Long accountId) {
        Account account = findAccountById.apply(accountId);
        if (account == null) {
            throw new EntityNotFoundException(String.format(ACCOUNT_NOT_FOUND_MESSAGE, accountId));
        }
        return account;
    }

    private Account ensureAccountPresent(Function<Long, Optional<Account>> findAccountById,
                                         Long accountId) {
        Optional<Account> account = findAccountById.apply(accountId);
        if (!account.isPresent()) {
            throw new EntityNotFoundException(String.format(ACCOUNT_NOT_FOUND_MESSAGE, accountId));
        }
        return account.get();
    }

}
