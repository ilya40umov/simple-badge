package com.ilya40umov.badge.dto;

import java.time.Instant;

/**
 * DTO that corresponds to {@link com.ilya40umov.badge.entity.AccountBadge} entity.
 *
 * @author isorokoumov
 */
public class AccountBadgeDto {

    private BadgeDto badge;
    private String comment;
    private Instant assigned;

    public BadgeDto getBadge() {
        return badge;
    }

    public AccountBadgeDto setBadge(BadgeDto badge) {
        this.badge = badge;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public AccountBadgeDto setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public Instant getAssigned() {
        return assigned;
    }

    public AccountBadgeDto setAssigned(Instant assigned) {
        this.assigned = assigned;
        return this;
    }

    @Override
    public String toString() {
        return "AccountBadgeDto{" +
                "badge=" + badge +
                ", comment='" + comment + '\'' +
                ", assigned=" + assigned +
                '}';
    }
}
