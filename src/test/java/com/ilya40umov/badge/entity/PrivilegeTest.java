package com.ilya40umov.badge.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

/**
 * @author isorokoumov
 */
@RunWith(JUnit4.class)
public class PrivilegeTest {

    @Test
    public void fromId_looksUpPrivilegeById() throws Exception {
        assertThat(Privilege.fromId(Privilege.ADMINISTER.getPrivilegeId()))
                .isEqualTo(Privilege.ADMINISTER);
    }

    @Test
    public void fromId_throwsExceptionOnUnknownId() throws Exception {
        assertThatThrownBy(() -> Privilege.fromId(1234567))
                .isInstanceOf(EnumConstantNotPresentException.class);
    }
}
