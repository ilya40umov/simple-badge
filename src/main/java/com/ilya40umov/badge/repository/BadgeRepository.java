package com.ilya40umov.badge.repository;

import com.ilya40umov.badge.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * DAO for {@link Badge} entity.
 *
 * @author isorokoumov
 */
public interface BadgeRepository extends JpaRepository<Badge, Long> {

    Optional<Badge> findByTitle(String title);

}
