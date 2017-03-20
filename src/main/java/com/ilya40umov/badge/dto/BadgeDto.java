package com.ilya40umov.badge.dto;

import com.fasterxml.jackson.annotation.JsonView;

import java.time.Instant;

/**
 * DTO that corresponds to {@link com.ilya40umov.badge.entity.Badge} entity.
 *
 * @author isorokoumov
 */
public class BadgeDto {

    private Long badgeId;
    private String title;
    private String description;
    private String thumbnailUrl;
    private Instant created;
    private Instant modified;
    private Long ownerId;

    @JsonView(Views.Public.class)
    public Long getBadgeId() {
        return badgeId;
    }

    public BadgeDto setBadgeId(Long badgeId) {
        this.badgeId = badgeId;
        return this;
    }

    @JsonView(Views.Public.class)
    public String getTitle() {
        return title;
    }

    public BadgeDto setTitle(String title) {
        this.title = title;
        return this;
    }

    @JsonView(Views.Public.class)
    public String getDescription() {
        return description;
    }

    public BadgeDto setDescription(String description) {
        this.description = description;
        return this;
    }

    @JsonView(Views.Public.class)
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public BadgeDto setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
        return this;
    }

    @JsonView(Views.Private.class)
    public Instant getCreated() {
        return created;
    }

    public BadgeDto setCreated(Instant created) {
        this.created = created;
        return this;
    }

    @JsonView(Views.Private.class)
    public Instant getModified() {
        return modified;
    }

    public BadgeDto setModified(Instant modified) {
        this.modified = modified;
        return this;
    }

    @JsonView(Views.Private.class)
    public Long getOwnerId() {
        return ownerId;
    }

    public BadgeDto setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    @Override
    public String toString() {
        return "BadgeDto{" +
                "badgeId=" + badgeId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                ", ownerId=" + ownerId +
                '}';
    }
}
