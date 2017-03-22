package com.ilya40umov.badge.repository;

import com.ilya40umov.badge.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * DAO for {@link Badge} entity.
 *
 * @author isorokoumov
 */
@Transactional(propagation = Propagation.MANDATORY)
public interface BadgeRepository extends JpaRepository<Badge, Long> {

    Optional<Badge> findByTitle(String title);

}
