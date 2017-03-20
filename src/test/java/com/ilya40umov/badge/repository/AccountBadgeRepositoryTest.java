package com.ilya40umov.badge.repository;

import com.ilya40umov.badge.entity.Account;
import com.ilya40umov.badge.entity.AccountBadge;
import com.ilya40umov.badge.entity.Badge;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.ilya40umov.badge.util.TestAccountBuilder.testAccountBuilder;
import static com.ilya40umov.badge.util.TestBadgeBuilder.testBadgeBuilder;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author isorokoumov
 */
@RunWith(SpringRunner.class)
@RepositoryTest
public class AccountBadgeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountBadgeRepository accountBadgeRepository;

    @Test
    public void findByAccountIdOrderByCreatedAsc_returnsOrderedBadgesForAccount() throws Exception {
        Account account1 = entityManager.persist(testAccountBuilder().build());
        Account account2 = entityManager.persist(testAccountBuilder()
                .withEmail("another@abc.com").build());
        Badge badge1 = entityManager.persist(testBadgeBuilder()
                .withOwner(account2).build());
        Badge badge2 = entityManager.persist(testBadgeBuilder()
                .withOwner(account2).withTitle("Another Badge").build());
        Badge badge3 = entityManager.persist(testBadgeBuilder()
                .withOwner(account2).withTitle("Alien Badge").build());
        entityManager.persist(new AccountBadge().setAccount(account1).setBadge(badge1));
        entityManager.persist(new AccountBadge().setAccount(account1).setBadge(badge2));
        entityManager.persist(new AccountBadge().setAccount(account2).setBadge(badge3));
        entityManager.flush();

        List<AccountBadge> badges = accountBadgeRepository
                .findByAccountIdOrderByCreatedAsc(account1.getAccountId());

        assertThat(badges).hasSize(2);
        assertThat(badges.get(0).getCreated()).isLessThan(badges.get(1).getCreated());
    }
}
