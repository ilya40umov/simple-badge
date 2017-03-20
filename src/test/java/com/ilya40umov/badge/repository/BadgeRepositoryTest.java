package com.ilya40umov.badge.repository;

import com.ilya40umov.badge.entity.Badge;
import com.ilya40umov.badge.util.TestBadgeBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static com.ilya40umov.badge.util.TestBadgeBuilder.testBadgeBuilder;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author isorokoumov
 */
@RunWith(SpringRunner.class)
@RepositoryTest
public class BadgeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BadgeRepository badgeRepository;

    @Test
    public void findByTitle_looksUpBadgeByTitle() throws Exception {
        entityManager.persist(testBadgeBuilder().withAutoCreatedOwner(entityManager).build());

        Optional<Badge> badge = badgeRepository.findByTitle(TestBadgeBuilder.DEFAULT_BADGE_TITLE);

        assertThat(badge).isNotEmpty();
    }
}
