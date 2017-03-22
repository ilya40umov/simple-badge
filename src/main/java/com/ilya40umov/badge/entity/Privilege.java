package com.ilya40umov.badge.entity;

import java.util.Optional;

/**
 * Represents a permission for a user to execute certain types of operations.
 *
 * @author isorokoumov
 */
public enum Privilege {

    /**
     * Allows creating new badges.
     */
    CREATE_BADGE(1, "ROLE_CREATE_BADGE"),
    /**
     * Allows performing administrative actions.
     */
    ADMINISTER(999, "ROLE_ADMIN");

    public static Optional<Privilege> fromId(Integer privilegeId) {
        for (Privilege privilege : Privilege.values()) {
            if (privilege.getPrivilegeId().equals(privilegeId)) {
                return Optional.of(privilege);
            }
        }
        return Optional.empty();
    }

    private final Integer privilegeId;
    private final String authorityName;

    Privilege(Integer privilegeId, String authorityName) {
        this.privilegeId = privilegeId;
        this.authorityName = authorityName;
    }

    public Integer getPrivilegeId() {
        return privilegeId;
    }

    public String getAuthorityName() {
        return authorityName;
    }
}
