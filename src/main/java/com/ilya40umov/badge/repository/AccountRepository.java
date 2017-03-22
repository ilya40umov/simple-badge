package com.ilya40umov.badge.repository;

import com.ilya40umov.badge.entity.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * DAO for {@link Account} entity.
 *
 * @author isorokoumov
 */
@Transactional(propagation = Propagation.MANDATORY)
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    @EntityGraph(value = "Account.withAllJoins", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select a from Account a where a.email = :email")
    Optional<Account> findByEmailWithAllJoins(@Param("email") String email);

    @EntityGraph(value = "Account.withPrivileges", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select a from Account a where a.email = :email")
    Optional<Account> findByEmailWithPrivileges(@Param("email") String email);

}
