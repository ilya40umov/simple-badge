package com.ilya40umov.badge.repository;

import com.ilya40umov.badge.entity.AccountBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * DAO for {@link AccountBadge} entity.
 *
 * @author isorokoumov
 */
public interface AccountBadgeRepository extends JpaRepository<AccountBadge, Long> {

    @Query("select ab from AccountBadge ab where ab.account.accountId = :accountId " +
            "order by ab.created asc")
    List<AccountBadge> findByAccountIdOrderByCreatedAsc(@Param("accountId") Long accountId);

}
