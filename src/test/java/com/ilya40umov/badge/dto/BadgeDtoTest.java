package com.ilya40umov.badge.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;

import static com.ilya40umov.badge.dto.JacksonTestUtils.useView;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author isorokoumov
 */
@RunWith(SpringRunner.class)
@JsonTest
public class BadgeDtoTest {

    @Autowired
    private JacksonTester<BadgeDto> jacksonTester;

    @Test
    public void badgeDto_isProperlySerialized_forPublicView() throws Exception {
        useView(jacksonTester, Views.Public.class);

        JsonContent<BadgeDto> serialized = jacksonTester.write(new BadgeDto().setBadgeId(1L)
                .setTitle("title").setDescription("description").setThumbnailUrl("url")
                .setCreated(Instant.EPOCH).setModified(Instant.EPOCH).setOwnerId(123L));

        assertThat(serialized).extractingJsonPathNumberValue("@.badgeId").isEqualTo(1);
        assertThat(serialized).extractingJsonPathStringValue("@.title").isEqualTo("title");
        assertThat(serialized).extractingJsonPathStringValue("@.description")
                .isEqualTo("description");
        assertThat(serialized).extractingJsonPathStringValue("@.thumbnailUrl").isEqualTo("url");
        assertThat(serialized).doesNotHaveJsonPathValue("@.created");
        assertThat(serialized).doesNotHaveJsonPathValue("@.modified");
        assertThat(serialized).doesNotHaveJsonPathValue("@.ownerId");
    }

    @Test
    public void badgeDto_isProperlySerialized_forPrivateView() throws Exception {
        useView(jacksonTester, Views.Private.class);

        JsonContent<BadgeDto> serialized = jacksonTester.write(new BadgeDto().setBadgeId(1L)
                .setTitle("title").setDescription("description").setThumbnailUrl("url")
                .setCreated(Instant.EPOCH).setModified(Instant.EPOCH).setOwnerId(123L));

        assertThat(serialized).extractingJsonPathNumberValue("@.badgeId").isEqualTo(1);
        assertThat(serialized).extractingJsonPathStringValue("@.title").isEqualTo("title");
        assertThat(serialized).extractingJsonPathStringValue("@.description")
                .isEqualTo("description");
        assertThat(serialized).extractingJsonPathStringValue("@.thumbnailUrl").isEqualTo("url");
        assertThat(serialized).extractingJsonPathStringValue("@.created")
                .isEqualTo(Instant.EPOCH.toString());
        assertThat(serialized).extractingJsonPathStringValue("@.modified")
                .isEqualTo(Instant.EPOCH.toString());
        assertThat(serialized).extractingJsonPathNumberValue("@.ownerId").isEqualTo(123);
    }
}
