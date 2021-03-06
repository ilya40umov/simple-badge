package com.ilya40umov.badge.repository;

import com.ilya40umov.badge.entity.AccountPrivilege;
import com.ilya40umov.badge.entity.AccountPrivilegeId;
import com.ilya40umov.badge.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO for {@link AccountPrivilege} entity.
 *
 * @author isorokoumov
 */
@Transactional(propagation = Propagation.MANDATORY)
public interface AccountPrivilegeRepository
        extends JpaRepository<AccountPrivilege, AccountPrivilegeId> {

    @Query("select CASE WHEN count(ap) > 0 THEN true ELSE false END from AccountPrivilege ap " +
            "where ap.account.accountId = :accountId " +
            "and ap.accountPrivilegeId.privilege = :privilege")
    boolean doesAccountHavePrivilege(@Param("accountId") Long accountId,
                                     @Param("privilege") Privilege privilege);

    @Query("select count(ap) from AccountPrivilege ap " +
            "where ap.accountPrivilegeId.privilege = :privilege")
    long countByPrivilege(@Param("privilege") Privilege privilege);
}
