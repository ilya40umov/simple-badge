package com.ilya40umov.badge.repository;

import com.ilya40umov.badge.entity.Account;
import com.ilya40umov.badge.entity.Privilege;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static com.ilya40umov.badge.util.TestAccountBuilder.testAccountBuilder;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author isorokoumov
 */
@RunWith(SpringRunner.class)
@RepositoryTest
public class AccountPrivilegeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountPrivilegeRepository accountPrivilegeRepository;

    @Test
    public void doesAccountHavePrivilege_returnsFalseIfAccountHasNoSuchPrivilege()
            throws Exception {
        Account account = entityManager.persist(testAccountBuilder().build());

        assertThat(accountPrivilegeRepository.doesAccountHavePrivilege(account.getAccountId(),
                Privilege.CREATE_BADGE)).isFalse();
    }

    @Test
    public void doesAccountHavePrivilege_returnsTrueIfAccountHasPrivilege() throws Exception {
        Account account = entityManager.persist(testAccountBuilder()
                .withCreateBadgePrivilege().build());

        assertThat(accountPrivilegeRepository.doesAccountHavePrivilege(account.getAccountId(),
                Privilege.CREATE_BADGE)).isTrue();
    }

    @Test
    public void countByPrivilege_returnsCountOfAccountsWithPrivilege() throws Exception {
        entityManager.persist(testAccountBuilder().withCreateBadgePrivilege().build());

        assertThat(accountPrivilegeRepository.countByPrivilege(Privilege.CREATE_BADGE))
                .isEqualTo(1);
    }
}
