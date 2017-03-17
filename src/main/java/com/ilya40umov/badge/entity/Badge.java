package com.ilya40umov.badge.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Blob;
import java.time.Instant;
import java.util.Set;

/**
 * Represents a badge that can be awarded by its owner to other users.
 *
 * @author isorokoumov
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "badge",
        indexes = {@Index(name = "badge_by_title", columnList = "title", unique = true)})
public class Badge {

    private Long badgeId;

    private String title;
    private String description;
    private Blob thumbnailImage;
    private Blob fullImage;

    private Instant created;
    private Instant modified;

    private Account owner;
    private Set<AccountBadge> accountBadges;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "badge_id", updatable = false, nullable = false)
    public Long getBadgeId() {
        return badgeId;
    }

    public Badge setBadgeId(Long badgeId) {
        this.badgeId = badgeId;
        return this;
    }

    @Column(name = "title", length = 64, nullable = false)
    public String getTitle() {
        return title;
    }

    public Badge setTitle(String title) {
        this.title = title;
        return this;
    }

    @Column(name = "description", length = 512, nullable = false)
    public String getDescription() {
        return description;
    }

    public Badge setDescription(String description) {
        this.description = description;
        return this;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "thumbnail_image")
    public Blob getThumbnailImage() {
        return thumbnailImage;
    }

    public Badge setThumbnailImage(Blob thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
        return this;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "full_image")
    public Blob getFullImage() {
        return fullImage;
    }

    public Badge setFullImage(Blob fullImage) {
        this.fullImage = fullImage;
        return this;
    }

    @CreatedDate
    @Column(name = "created", columnDefinition = "datetime(3)", nullable = false, updatable = false)
    public Instant getCreated() {
        return created;
    }

    public Badge setCreated(Instant created) {
        this.created = created;
        return this;
    }

    @LastModifiedDate
    @Column(name = "modified", columnDefinition = "datetime(3)", nullable = false)
    public Instant getModified() {
        return modified;
    }

    public Badge setModified(Instant modified) {
        this.modified = modified;
        return this;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_account_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_badge_owner_account_id"))
    public Account getOwner() {
        return owner;
    }

    public Badge setOwner(Account owner) {
        this.owner = owner;
        return this;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "badge", orphanRemoval = true)
    public Set<AccountBadge> getAccountBadges() {
        return accountBadges;
    }

    public Badge setAccountBadges(Set<AccountBadge> accountBadges) {
        this.accountBadges = accountBadges;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Badge)) return false;

        Badge badge = (Badge) o;

        return getTitle() != null ? getTitle().equals(badge.getTitle()) : badge.getTitle() == null;
    }

    @Override
    public int hashCode() {
        return getTitle() != null ? getTitle().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Badge{" +
                "badgeId=" + badgeId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", thumbnailImage=" + thumbnailImage +
                ", fullImage=" + fullImage +
                ", created=" + created +
                ", modified=" + modified +
                ", ownerId=" + owner.getAccountId() +
                '}';
    }
}
