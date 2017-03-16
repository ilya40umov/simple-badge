package com.ilya40umov.badge.entity;

/**
 * Represents a permission for a user to execute certain types of operations.
 *
 * @author isorokoumov
 */
public enum Privilege {

    /**
     * Allows creating new badges.
     */
    CREATE_BADGE(1),
    /**
     * Allows performing administrative actions.
     */
    ADMINISTER(999);

    public static Privilege fromId(Integer privilegeId) {
        for (Privilege privilege : Privilege.values()) {
            if (privilege.getPrivilegeId().equals(privilegeId)) {
                return privilege;
            }
        }
        throw new EnumConstantNotPresentException(Privilege.class, "privilegeId:" + privilegeId);
    }

    private final Integer privilegeId;

    Privilege(Integer privilegeId) {
        this.privilegeId = privilegeId;
    }

    public Integer getPrivilegeId() {
        return privilegeId;
    }
}
