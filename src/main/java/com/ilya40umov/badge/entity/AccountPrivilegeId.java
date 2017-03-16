package com.ilya40umov.badge.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Natural primary key for {@link AccountPrivilege}.
 *
 * @author isorokoumov
 */
@Embeddable
public class AccountPrivilegeId implements Serializable {

    private Privilege privilege;
    private Long accountId;

    public Privilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountPrivilegeId)) return false;

        AccountPrivilegeId that = (AccountPrivilegeId) o;

        if (getPrivilege() != that.getPrivilege()) return false;
        return getAccountId() != null ? getAccountId().equals(that.getAccountId()) : that
                .getAccountId() == null;
    }

    @Override
    public int hashCode() {
        int result = getPrivilege() != null ? getPrivilege().hashCode() : 0;
        result = 31 * result + (getAccountId() != null ? getAccountId().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AccountPrivilegeId{" +
                "privilege=" + privilege +
                ", accountId=" + accountId +
                '}';
    }
}