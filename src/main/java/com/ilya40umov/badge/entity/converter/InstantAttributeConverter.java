package com.ilya40umov.badge.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * Converts {@link java.time.Instant} to {@link java.sql.Timestamp} and vice versa,
 * thus allowing JPA to store Instant as a datetime.
 *
 * @author isorokoumov
 */
@Converter(autoApply = true)
public class InstantAttributeConverter implements AttributeConverter<Instant, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(Instant attribute) {
        return attribute != null ? Timestamp.from(attribute) : null;
    }

    @Override
    public Instant convertToEntityAttribute(Timestamp dbData) {
        return dbData != null ? Instant.ofEpochMilli(dbData.getTime()) : null;
    }
}
