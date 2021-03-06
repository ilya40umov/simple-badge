package com.ilya40umov.badge.entity.converter;

import com.ilya40umov.badge.entity.Privilege;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;

/**
 * Converts {@link Privilege} to {@link Integer} and vice versa, thus allowing to store a custom int
 * value in db instead of enum ordinal/name(which tend to change with time).
 *
 * @author isorokoumov
 */
@Converter(autoApply = true)
public class PrivilegeAttributeConverter implements AttributeConverter<Privilege, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Privilege attribute) {
        return attribute != null ? attribute.getPrivilegeId() : null;
    }

    @Override
    public Privilege convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return Privilege.fromId(dbData).orElseThrow(
                () -> new EnumConstantNotPresentException(Privilege.class, String.valueOf(dbData)));
    }
}
