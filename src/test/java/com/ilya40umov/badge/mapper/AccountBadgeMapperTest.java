package com.ilya40umov.badge.mapper;

import com.google.common.collect.ImmutableSet;
import com.ilya40umov.badge.dto.AccountBadgeDto;
import com.ilya40umov.badge.dto.BadgeDto;
import com.ilya40umov.badge.entity.Account;
import com.ilya40umov.badge.entity.AccountBadge;
import com.ilya40umov.badge.entity.Badge;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author isorokoumov
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MapperTestConfig.class)
public class AccountBadgeMapperTest {

    private static final Long ACCOUNT_ID = 123L;
    private static final String ACCOUNT_EMAIL = "email@gmail.com";
    private static final Long BADGE_ID_1 = 333L;
    private static final String BADGE_TITLE_1 = "Title1";
    private static final Long BADGE_ID_2 = 444L;
    private static final String BADGE_TITLE_2 = "Title2";
    private static final String COMMENT = "comment";

    @Autowired
    private AccountBadgeMapper mapper;

    @Test
    public void toAccountBadgeDto_transformsEntityToDto() throws Exception {
        AccountBadgeDto dto = mapper.toAccountBadgeDto(
                createAccountBadge(BADGE_ID_1, BADGE_TITLE_1));

        assertThat(dto).isEqualToComparingFieldByFieldRecursively(new AccountBadgeDto()
                .setComment(COMMENT)
                .setAssigned(Instant.EPOCH)
                .setBadge(new BadgeDto().setBadgeId(BADGE_ID_1).setTitle(BADGE_TITLE_1)));
    }

    @Test
    public void toAccountBadgeDtos_transformsSetOfEntitiesToListOfDtos() throws Exception {
        List<AccountBadgeDto> dtos = mapper.toAccountBadgeDtos(ImmutableSet.of(
                createAccountBadge(BADGE_ID_1, BADGE_TITLE_1),
                createAccountBadge(BADGE_ID_2, BADGE_TITLE_2)));

        assertThat(dtos).hasSize(2);
    }

    private AccountBadge createAccountBadge(Long badgeId, String badgeTitle) {
        return new AccountBadge()
                .setComment(COMMENT)
                .setCreated(Instant.EPOCH)
                .setAccount(new Account().setAccountId(ACCOUNT_ID).setEmail(ACCOUNT_EMAIL))
                .setBadge(new Badge().setBadgeId(badgeId).setTitle(badgeTitle));
    }

}
