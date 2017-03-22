package com.ilya40umov.badge.mapper;

import com.ilya40umov.badge.dto.BadgeDto;
import com.ilya40umov.badge.entity.Account;
import com.ilya40umov.badge.entity.Badge;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * @author isorokoumov
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MapperTestConfig.class)
public class BadgeMapperTest {

    private static final Long BADGE_ID = 123L;
    private static final String BADGE_TITLE = "title";
    private static final String BADGE_DESCRIPTION = "description";
    private static final Long OWNER_ID = 12345L;

    @Autowired
    private BadgeMapper mapper;

    @Test
    public void toBadgeDto_transformsEntityToDto() throws Exception {
        BadgeDto dto = mapper.toBadgeDto(new Badge()
                .setBadgeId(BADGE_ID)
                .setTitle(BADGE_TITLE)
                .setDescription(BADGE_DESCRIPTION)
                .setCreated(Instant.EPOCH)
                .setModified(Instant.EPOCH)
                .setOwner(new Account().setAccountId(OWNER_ID)));

        assertThat(dto).isEqualToComparingFieldByField(new BadgeDto()
                .setBadgeId(BADGE_ID)
                .setTitle(BADGE_TITLE)
                .setDescription(BADGE_DESCRIPTION)
                .setCreated(Instant.EPOCH)
                .setModified(Instant.EPOCH)
                .setOwnerId(OWNER_ID));
    }
}
