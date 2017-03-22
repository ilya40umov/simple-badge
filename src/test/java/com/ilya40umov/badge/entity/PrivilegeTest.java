package com.ilya40umov.badge.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author isorokoumov
 */
@RunWith(JUnit4.class)
public class PrivilegeTest {

    @Test
    public void fromId_returnsOptionalWithPrivilege_forKnownId() throws Exception {
        assertThat(Privilege.fromId(Privilege.ADMINISTER.getPrivilegeId()))
                .hasValue(Privilege.ADMINISTER);
    }

    @Test
    public void fromId_returnsEmptyOptional_forUnknownId() throws Exception {
        assertThat(Privilege.fromId(1234567)).isEmpty();
    }
}
