package com.ilya40umov.badge.util;

import com.ilya40umov.badge.entity.Account;
import com.ilya40umov.badge.entity.AccountBadge;
import com.ilya40umov.badge.entity.Badge;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.sql.Blob;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.ilya40umov.badge.util.TestAccountBuilder.testAccountBuilder;
import static org.assertj.core.util.Preconditions.checkNotNull;

/**
 * @author isorokoumov
 */
public class TestBadgeBuilder {

    public static final String DEFAULT_BADGE_TITLE = "Shiny Badge";
    public static final String DEFAULT_BADGE_DESCRIPTION = "Very very shiny badge that glows";

    private Long badgeId;

    private String title = DEFAULT_BADGE_TITLE;
    private String description = DEFAULT_BADGE_DESCRIPTION;
    private Blob thumbnailImage;
    private Blob fullImage;

    private Instant created;
    private Instant modified;

    private Account owner;
    private Set<AccountBadge> accountBadges = new LinkedHashSet<>();

    public static TestBadgeBuilder testBadgeBuilder() {
        return new TestBadgeBuilder();
    }

    public TestBadgeBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public TestBadgeBuilder withOwner(Account account) {
        this.owner = account;
        return this;
    }

    public TestBadgeBuilder withAutoCreatedOwner(TestEntityManager entityManager) {
        Account account = entityManager.persist(testAccountBuilder().build());
        return withOwner(account);
    }

    public Badge build() {
        checkNotNull(owner, "Owner must be set");
        checkNotNull(title);
        checkNotNull(description);
        Badge badge = new Badge().setBadgeId(badgeId).setTitle(title).setDescription(description)
                .setThumbnailImage(thumbnailImage).setFullImage(fullImage)
                .setCreated(created).setModified(modified)
                .setOwner(owner);
        accountBadges.forEach(ab -> ab.setBadge(badge));
        return badge.setAccountBadges(accountBadges);
    }
}
