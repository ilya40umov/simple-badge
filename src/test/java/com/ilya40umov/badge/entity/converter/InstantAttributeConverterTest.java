package com.ilya40umov.badge.entity.converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.Timestamp;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author isorokoumov
 */
@RunWith(JUnit4.class)
public class InstantAttributeConverterTest {

    private static final Instant NOW_AS_INSTANT = Instant.now();
    private static final Timestamp NOW_AS_TIMESTAMP = Timestamp.from(NOW_AS_INSTANT);

    private final InstantAttributeConverter converter = new InstantAttributeConverter();

    @Test
    public void convertToDatabaseColumn_turnsInstantToTimestamp() throws Exception {
        assertThat(converter.convertToDatabaseColumn(NOW_AS_INSTANT)).isEqualTo(NOW_AS_TIMESTAMP);
    }

    @Test
    public void convertToDatabaseColumn_turnsNullToNull() throws Exception {
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
    }

    @Test
    public void convertToEntityAttribute_turnsTimestampToInstant() throws Exception {
        assertThat(converter.convertToEntityAttribute(NOW_AS_TIMESTAMP)).isEqualTo(NOW_AS_INSTANT);
    }

    @Test
    public void convertToEntityAttribute_turnsNullToNull() throws Exception {
        assertThat(converter.convertToEntityAttribute(null)).isNull();
    }
}
