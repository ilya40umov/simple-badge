package com.ilya40umov.badge.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author isorokoumov
 */
@RunWith(SpringRunner.class)
@JsonTest
public class AccountBadgeDtoTest {

    @Autowired
    private JacksonTester<AccountBadgeDto> jacksonTester;

    @Test
    public void accountBadgeDto_isProperlySerialized() throws Exception {
        JsonContent<AccountBadgeDto> serialized = jacksonTester.write(new AccountBadgeDto()
                .setBadge(new BadgeDto().setBadgeId(1L)).setComment("comment")
                .setAssigned(Instant.EPOCH));

        assertThat(serialized).extractingJsonPathNumberValue("@.badge.badgeId").isEqualTo(1);
        assertThat(serialized).extractingJsonPathStringValue("@.comment").isEqualTo("comment");
        assertThat(serialized).extractingJsonPathStringValue("@.assigned")
                .isEqualTo(Instant.EPOCH.toString());
    }

}
