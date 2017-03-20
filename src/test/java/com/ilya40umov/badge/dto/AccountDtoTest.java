package com.ilya40umov.badge.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Collections;

import static com.ilya40umov.badge.dto.JacksonTestUtils.useView;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author isorokoumov
 */
@RunWith(SpringRunner.class)
@JsonTest
public class AccountDtoTest {

    @Autowired
    private JacksonTester<AccountDto> jacksonTester;

    @Test
    public void accountDto_isProperlySerialized_forPublicView() throws Exception {
        useView(jacksonTester, Views.Public.class);

        JsonContent<AccountDto> serialized = jacksonTester.write(new AccountDto().setAccountId(1L)
                .setEmail("email").setFirstName("fname").setLastName("lname")
                .setCreated(Instant.EPOCH).setModified(Instant.EPOCH).setCanCreateBadges(true)
                .setAccountBadges(Collections.singletonList(new AccountBadgeDto()
                        .setBadge(new BadgeDto().setBadgeId(1L).setOwnerId(123L))
                        .setComment("comment"))));

        assertThat(serialized).extractingJsonPathNumberValue("@.accountId").isEqualTo(1);
        assertThat(serialized).doesNotHaveJsonPathValue("@.email");
        assertThat(serialized).extractingJsonPathStringValue("@.firstName").isEqualTo("fname");
        assertThat(serialized).extractingJsonPathStringValue("@.lastName").isEqualTo("lname");
        assertThat(serialized).doesNotHaveJsonPathValue("@.created");
        assertThat(serialized).doesNotHaveJsonPathValue("@.modified");
        assertThat(serialized).extractingJsonPathStringValue("@.accountBadges[0].comment")
                .isEqualTo("comment");
        assertThat(serialized)
                .extractingJsonPathNumberValue("@.accountBadges[0].badge.badgeId")
                .isEqualTo(1);
        assertThat(serialized).doesNotHaveJsonPathValue("@.accountBadges[0].badge.ownerId");

    }

    @Test
    public void accountDto_isProperlySerialized_forPrivateView() throws Exception {
        useView(jacksonTester, Views.Private.class);

        JsonContent<AccountDto> serialized = jacksonTester.write(new AccountDto().setAccountId(1L)
                .setEmail("email").setFirstName("fname").setLastName("lname")
                .setCreated(Instant.EPOCH).setModified(Instant.EPOCH).setCanCreateBadges(true)
                .setAccountBadges(Collections.singletonList(new AccountBadgeDto()
                        .setBadge(new BadgeDto().setBadgeId(1L).setOwnerId(123L))
                        .setComment("comment"))));

        assertThat(serialized).extractingJsonPathNumberValue("@.accountId").isEqualTo(1);
        assertThat(serialized).extractingJsonPathStringValue("@.email").isEqualTo("email");
        assertThat(serialized).extractingJsonPathStringValue("@.firstName").isEqualTo("fname");
        assertThat(serialized).extractingJsonPathStringValue("@.lastName").isEqualTo("lname");
        assertThat(serialized).extractingJsonPathStringValue("@.created")
                .isEqualTo(Instant.EPOCH.toString());
        assertThat(serialized).extractingJsonPathStringValue("@.modified")
                .isEqualTo(Instant.EPOCH.toString());
        assertThat(serialized).extractingJsonPathStringValue("@.accountBadges[0].comment")
                .isEqualTo("comment");
        assertThat(serialized)
                .extractingJsonPathNumberValue("@.accountBadges[0].badge.badgeId")
                .isEqualTo(1);
        assertThat(serialized)
                .extractingJsonPathNumberValue("@.accountBadges[0].badge.ownerId")
                .isEqualTo(123);
    }
}
