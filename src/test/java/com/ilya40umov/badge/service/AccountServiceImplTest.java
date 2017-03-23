package com.ilya40umov.badge.service;

import com.ilya40umov.badge.dto.AccountDto;
import com.ilya40umov.badge.dto.command.CreateAccount;
import com.ilya40umov.badge.dto.partial.AccountDetails;
import com.ilya40umov.badge.dto.partial.PasswordDetails;
import com.ilya40umov.badge.entity.Account;
import com.ilya40umov.badge.entity.AccountBadge;
import com.ilya40umov.badge.entity.Badge;
import com.ilya40umov.badge.repository.AccountRepository;
import com.ilya40umov.badge.service.exception.EntityNotFoundException;
import com.ilya40umov.badge.service.exception.InvalidRequestException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static com.ilya40umov.badge.entity.Privilege.CREATE_BADGE;
import static com.ilya40umov.badge.util.TestAccountBuilder.*;
import static com.ilya40umov.badge.util.TestBadgeBuilder.testBadgeBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author isorokoumov
 */
@RunWith(SpringRunner.class)
@ServiceTest
public class AccountServiceImplTest {

    private static final String ENCODED_PASSWORD = "encoded_password";
    private static final String ANOTHER_FIRST_NAME = "First Name";
    private static final String ANOTHER_LAST_NAME = "Last Name";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;

    @Before
    public void setUpPasswordEncoder() throws Exception {
        when(passwordEncoder.encode(DEFAULT_PASSWORD)).thenReturn(ENCODED_PASSWORD);
    }

    @Test
    public void findById_transformsAccountToDto_ifFound() throws Exception {
        Account account = accountRepository.save(testAccountBuilder()
                .withCreateBadgePrivilege().build());

        Optional<AccountDto> accountDto = accountService.findById(account.getAccountId());

        assertThat(accountDto).hasValueSatisfying(dto -> {
            assertThat(dto.getEmail()).isEqualTo(DEFAULT_EMAIL);
            assertThat(dto.getPrivilegeIds()).contains(CREATE_BADGE.getPrivilegeId());
        });
    }

    @Test
    public void findById_returnsEmptyOptional_ifNotFound() throws Exception {
        Optional<AccountDto> accountDto = accountService.findById(123456L);

        assertThat(accountDto).isEmpty();
    }

    @Test
    public void isEmailRegistered_returnsTrue_ifFindsAccountWithSuchEmail() throws Exception {
        accountRepository.save(testAccountBuilder().build());

        boolean isRegistered = accountService.isEmailRegistered(DEFAULT_EMAIL);

        assertThat(isRegistered).isTrue();
    }

    @Test
    public void isEmailRegistered_returnsFalse_ifCanNotFindAccountWithSuchEmail() throws Exception {
        boolean isRegistered = accountService.isEmailRegistered(DEFAULT_EMAIL);

        assertThat(isRegistered).isFalse();
    }

    @Test
    public void createAccount_savesToDbAndReturnsDtoWithId_ifSaveSucceeds() throws Exception {
        AccountDto accountDto = accountService.createAccount(new CreateAccount()
                .setAccount(new AccountDetails()
                        .setEmail(DEFAULT_EMAIL)
                        .setFirstName(DEFAULT_FIRST_NAME)
                        .setLastName(DEFAULT_LAST_NAME))
                .setPassword(new PasswordDetails()
                        .setNewPassword(DEFAULT_PASSWORD)));

        assertThat(accountDto).satisfies(dto -> {
            assertThat(dto.getAccountId()).isNotNull();
            assertThat(dto.getEmail()).isEqualTo(DEFAULT_EMAIL);
            assertThat(dto.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
            assertThat(dto.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        });
        assertThat(accountRepository.findOne(accountDto.getAccountId())).satisfies(account ->
                assertThat(account.getPassword()).isEqualTo(ENCODED_PASSWORD));
    }

    @Test
    public void createAccount_failsToSaveAccount_ifEmailIsAlreadyRegistered() throws Exception {
        accountRepository.save(testAccountBuilder().build());

        assertThatThrownBy(() ->
                accountService.createAccount(new CreateAccount()
                        .setAccount(new AccountDetails()
                                .setEmail(DEFAULT_EMAIL)
                                .setFirstName(ANOTHER_FIRST_NAME)
                                .setLastName(ANOTHER_LAST_NAME))
                        .setPassword(new PasswordDetails()
                                .setNewPassword("A different password")))
        ).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void updateAccount_modifiesEntityAndSavesToDb() throws Exception {
        Account originalAccount = accountRepository.save(testAccountBuilder().build());

        accountService.updateAccount(originalAccount.getAccountId(), new AccountDetails()
                .setEmail(DEFAULT_EMAIL)
                .setFirstName(ANOTHER_FIRST_NAME)
                .setLastName(ANOTHER_LAST_NAME));

        assertThat(accountRepository.findOne(originalAccount.getAccountId())).satisfies(account -> {
            assertThat(account.getFirstName()).isEqualTo(ANOTHER_FIRST_NAME);
            assertThat(account.getLastName()).isEqualTo(ANOTHER_LAST_NAME);
        });
    }

    @Test
    public void updateAccount_throwsException_ifAccountNotFound() throws Exception {
        assertThatThrownBy(() ->
                accountService.updateAccount(12345L, new AccountDetails())
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void updatePassword_changesPasswordToNew_ifCurrentPasswordMatches() throws Exception {
        Account originalAccount = accountRepository.save(testAccountBuilder().build());
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("Newly Encoded Password");

        accountService.updatePassword(originalAccount.getAccountId(), new PasswordDetails()
                .setCurrentPassword(DEFAULT_PASSWORD)
                .setNewPassword("a new password"));

        assertThat(accountRepository.findOne(originalAccount.getAccountId())).satisfies(account ->
                assertThat(account.getPassword()).isEqualTo("Newly Encoded Password")
        );
    }

    @Test
    public void updatePassword_throwsException_ifAccountNotFound() throws Exception {
        assertThatThrownBy(() ->
                accountService.updatePassword(12345L, new PasswordDetails())
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void updatePassword_throwsException_ifCurrentPasswordDoesNotMatch() throws Exception {
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        Account account = accountRepository.save(testAccountBuilder().build());

        assertThatThrownBy(() ->
                accountService.updatePassword(account.getAccountId(), new PasswordDetails()
                        .setCurrentPassword("invalid current password"))
        ).isInstanceOf(InvalidRequestException.class);
    }

    @Test
    public void deleteAccount_removesAccountFromDb_ifAccountIsFound() throws Exception {
        Account account = accountRepository.save(testAccountBuilder().build());

        accountService.deleteAccount(account.getAccountId());

        assertThat(accountRepository.findOne(account.getAccountId())).isNull();
    }

    @Test
    public void deleteAccount_throwsException_ifAccountNotFound() throws Exception {
        assertThatThrownBy(() ->
                accountService.deleteAccount(12345L)
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void listWithBadgeId_returnsOnlyAccountsThatHaveBadge() throws Exception {
        Account account = accountRepository.save(testAccountBuilder().build());
        Badge badge = entityManager.persist(testBadgeBuilder().withOwner(account).build());
        Account account1 = accountRepository.save(testAccountBuilder()
                .withEmail("email1@gmail.com").build());
        Account account2 = accountRepository.save(testAccountBuilder()
                .withEmail("email2@gmail.com").build());
        entityManager.persist(new AccountBadge().setAccount(account1).setBadge(badge));
        entityManager.persist(new AccountBadge().setAccount(account2).setBadge(badge));

        Page<AccountDto> accounts = accountService
                .listWithBadgeId(badge.getBadgeId(), new PageRequest(0, 10));

        assertThat(accounts).hasSize(2);
    }

    @Test
    public void addPrivilege_grantsAccountSpecifiedPrivilege_ifItDoesNotHaveIt() throws Exception {
        Account account = accountRepository.save(testAccountBuilder().build());

        accountService.addPrivilege(account.getAccountId(), CREATE_BADGE.getPrivilegeId());

        assertThat(accountRepository.findByIdWithPrivileges(account.getAccountId()))
                .hasValueSatisfying(acc ->
                        assertThat(acc.getAccountPrivileges()).hasSize(1)
                );
    }

    @Test
    public void addPrivilege_throwsException_ifPrivilegeIdInvalid() throws Exception {
        Account account = accountRepository.save(testAccountBuilder().build());

        assertThatThrownBy(() ->
                accountService.addPrivilege(account.getAccountId(), 99937)
        ).isInstanceOf(InvalidRequestException.class);
    }

    @Test
    public void addPrivilege_throwsException_ifAccountNotFound() throws Exception {
        assertThatThrownBy(() ->
                accountService.addPrivilege(12345L, CREATE_BADGE.getPrivilegeId())
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void addPrivilege_throwsException_ifPrivilegeAlreadyGranted() throws Exception {
        Account account = accountRepository.save(testAccountBuilder()
                .withCreateBadgePrivilege().build());

        assertThatThrownBy(() ->
                accountService.addPrivilege(account.getAccountId(), CREATE_BADGE.getPrivilegeId())
        ).isInstanceOf(InvalidRequestException.class);
    }

    @Test
    public void removePrivilege_removesSpecifiedPrivilegeFromAccount_ifItHasIt() throws Exception {
        Account account = accountRepository.save(testAccountBuilder()
                .withCreateBadgePrivilege().build());

        accountService.removePrivilege(account.getAccountId(), CREATE_BADGE.getPrivilegeId());

        assertThat(accountRepository.findByIdWithPrivileges(account.getAccountId()))
                .hasValueSatisfying(acc ->
                        assertThat(acc.getAccountPrivileges()).isEmpty()
                );
    }

    @Test
    public void removePrivilege_throwsException_ifPrivilegeIdInvalid() throws Exception {
        Account account = accountRepository.save(testAccountBuilder().build());

        assertThatThrownBy(() ->
                accountService.removePrivilege(account.getAccountId(), 99937)
        ).isInstanceOf(InvalidRequestException.class);
    }

    @Test
    public void removePrivilege_throwsException_ifAccountNotFound() throws Exception {
        assertThatThrownBy(() ->
                accountService.removePrivilege(12345L, CREATE_BADGE.getPrivilegeId())
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void removePrivilege_throwsException_ifPrivilegeNotYetGranted() throws Exception {
        Account account = accountRepository.save(testAccountBuilder().build());

        assertThatThrownBy(() ->
                accountService.removePrivilege(account.getAccountId(),
                        CREATE_BADGE.getPrivilegeId())
        ).isInstanceOf(InvalidRequestException.class);
    }

}
