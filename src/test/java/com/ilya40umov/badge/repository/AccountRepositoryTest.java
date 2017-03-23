package com.ilya40umov.badge.repository;

import com.ilya40umov.badge.entity.Account;
import com.ilya40umov.badge.entity.AccountBadge;
import com.ilya40umov.badge.entity.Badge;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Optional;

import static com.ilya40umov.badge.util.TestAccountBuilder.testAccountBuilder;
import static com.ilya40umov.badge.util.TestBadgeBuilder.testBadgeBuilder;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author isorokoumov
 */
@RunWith(SpringRunner.class)
@RepositoryTest
public class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void save_setsCreatedAndModifiedField_forNewEntity() throws Exception {
        // creating account where created&modified are both null
        Account accountWithNoCreated = testAccountBuilder().withEmptyCreatedAndModified().build();

        Account createdAccount = accountRepository.save(accountWithNoCreated);

        assertThat(createdAccount.getCreated()).isNotNull();
        assertThat(createdAccount.getModified()).isNotNull();
    }

    @Test
    public void save_updatesModifiedField() throws Exception {
        Account newAccount = testAccountBuilder().build();

        Account createdAccount = accountRepository.save(newAccount);
        Instant modifiedAfterCreation = createdAccount.getModified();
        Account updatedAccount = accountRepository.save(createdAccount.setLastName("test"));
        entityManager.flush();

        assertThat(updatedAccount.getModified()).isGreaterThan(modifiedAfterCreation);
    }

    @Test
    public void save_persistsPrivilegesOfAccount() {
        accountRepository.save(testAccountBuilder().withCreateBadgePrivilege().build());

        assertThat(entityManager.getEntityManager()
                .createQuery("select ap from AccountPrivilege ap").getResultList())
                .hasSize(1);
    }

    @Test
    public void save_removesOrphanedPrivilegesOfAccount() {
        Account account = accountRepository.save(testAccountBuilder().withAllPrivileges().build());
        entityManager.flush();
        account.removeAccountPrivilege(account.getAccountPrivileges().iterator().next());
        entityManager.merge(account);
        entityManager.flush();

        assertThat(entityManager.getEntityManager()
                .createQuery("select ap from AccountPrivilege ap").getResultList())
                .hasSize(1);
    }

    @Test
    public void findByIdWithPrivileges_loadsPrivileges() throws Exception {
        Account account = entityManager.persist(testAccountBuilder()
                .withCreateBadgePrivilege().build());
        entityManager.flush();
        entityManager.clear();

        Optional<Account> foundAccount = accountRepository
                .findByIdWithPrivileges(account.getAccountId());

        assertThat(foundAccount).isNotEmpty();
        assertThat(foundAccount).hasValueSatisfying(
                a -> assertThat(a.getAccountPrivileges()).hasSize(1));
    }

    @Test
    public void findByIdWithAllJoins_loadsAllJoins() throws Exception {
        Account account = entityManager.persist(testAccountBuilder()
                .withCreateBadgePrivilege().build());
        Badge badge = entityManager.persist(testBadgeBuilder().withOwner(account).build());
        entityManager.persist(new AccountBadge().setAccount(account).setBadge(badge));
        entityManager.flush();
        entityManager.clear();

        Optional<Account> foundAccount = accountRepository
                .findByIdWithAllJoins(account.getAccountId());

        assertThat(foundAccount).isNotEmpty();
        assertThat(foundAccount).hasValueSatisfying(a -> {
            assertThat(a.getAccountPrivileges()).hasSize(1);
            assertThat(a.getAccountBadges()).hasSize(1);
        });
    }

    @Test
    public void findByEmail_doesNotLoadLazyFields() throws Exception {
        Account account = entityManager.persist(testAccountBuilder()
                .withCreateBadgePrivilege().build());
        entityManager.clear();

        Optional<Account> foundAccount = accountRepository.findByEmail(account.getEmail());

        assertThat(foundAccount).isNotEmpty();
        assertThat(foundAccount).hasValueSatisfying(
                a -> assertThat(a.getAccountPrivileges()).isEmpty());
    }

    @Test
    public void findByEmailWithPrivileges_loadsPrivileges() throws Exception {
        Account account = entityManager.persist(testAccountBuilder()
                .withCreateBadgePrivilege().build());
        entityManager.flush();
        entityManager.clear();

        Optional<Account> foundAccount = accountRepository
                .findByEmailWithPrivileges(account.getEmail());

        assertThat(foundAccount).isNotEmpty();
        assertThat(foundAccount).hasValueSatisfying(
                a -> assertThat(a.getAccountPrivileges()).hasSize(1));
    }

    @Test
    public void findByEmailWithAllJoins_loadsAllJoins() throws Exception {
        Account account = entityManager.persist(testAccountBuilder()
                .withCreateBadgePrivilege().build());
        Badge badge = entityManager.persist(testBadgeBuilder().withOwner(account).build());
        entityManager.persist(new AccountBadge().setAccount(account).setBadge(badge));
        entityManager.flush();
        entityManager.clear();

        Optional<Account> foundAccount = accountRepository
                .findByEmailWithAllJoins(account.getEmail());

        assertThat(foundAccount).isNotEmpty();
        assertThat(foundAccount).hasValueSatisfying(a -> {
            assertThat(a.getAccountPrivileges()).hasSize(1);
            assertThat(a.getAccountBadges()).hasSize(1);
        });
    }

    @Test
    public void listWithBadgeId_returnsAccountsThatHaveBadge() throws Exception {
        Account badgeOwner = entityManager.persist(testAccountBuilder()
                .withCreateBadgePrivilege().build());
        Badge badge1 = entityManager.persist(testBadgeBuilder()
                .withTitle("Badge1").withOwner(badgeOwner).build());
        Badge badge2 = entityManager.persist(testBadgeBuilder()
                .withTitle("Badge2").withOwner(badgeOwner).build());
        Account account1 = entityManager.persist(testAccountBuilder().withEmail("a1@gmail.com")
                .withAllPrivileges().build());
        Account account2 = entityManager.persist(testAccountBuilder().withEmail("a2@gmail.com")
                .withAllPrivileges().build());
        entityManager.persist(new AccountBadge().setAccount(account1).setBadge(badge1));
        entityManager.persist(new AccountBadge().setAccount(account1).setBadge(badge2));
        entityManager.persist(new AccountBadge().setAccount(account2).setBadge(badge1));

        Page<Account> accounts = accountRepository.listWithBadgeId(badge1.getBadgeId(),
                new PageRequest(0, 50));

        assertThat(accounts).hasSize(2);
    }

}