package com.ilya40umov.badge.entity.converter;

import com.ilya40umov.badge.entity.Privilege;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author isorokoumov
 */
@RunWith(JUnit4.class)
public class PrivilegeAttributeConverterTest {

    private final PrivilegeAttributeConverter converter = new PrivilegeAttributeConverter();

    @Test
    public void convertToDatabaseColumn_turnsPrivilegeToId() throws Exception {
        assertThat(converter.convertToDatabaseColumn(Privilege.ADMINISTER))
                .isEqualTo(Privilege.ADMINISTER.getPrivilegeId());
    }

    @Test
    public void convertToDatabaseColumn_turnsNullToNull() throws Exception {
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
    }

    @Test
    public void convertToEntityAttribute_turnsIdToPrivilege() throws Exception {
        assertThat(converter.convertToEntityAttribute(Privilege.CREATE_BADGE.getPrivilegeId()))
                .isEqualTo(Privilege.CREATE_BADGE);
    }

    @Test
    public void convertToEntityAttribute_turnsNullToNull() throws Exception {
        assertThat(converter.convertToEntityAttribute(null)).isNull();
    }
}
