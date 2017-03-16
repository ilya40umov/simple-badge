package com.ilya40umov.badge.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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

    public void setBadgeId(Long badgeId) {
        this.badgeId = badgeId;
    }

    @Column(name = "title", length = 64, nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "description", length = 512, nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "thumbnail_image")
    public Blob getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(Blob thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "full_image")
    public Blob getFullImage() {
        return fullImage;
    }

    public void setFullImage(Blob fullImage) {
        this.fullImage = fullImage;
    }

    @CreatedDate
    @Column(name = "created", columnDefinition = "datetime(3)", nullable = false, updatable = false)
    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    @LastModifiedDate
    @Column(name = "modified", columnDefinition = "datetime(3)", nullable = false)
    public Instant getModified() {
        return modified;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_account_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_badge_owner_account_id"))
    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "badge", orphanRemoval = true)
    public Set<AccountBadge> getAccountBadges() {
        return accountBadges;
    }

    public void setAccountBadges(Set<AccountBadge> accountBadges) {
        this.accountBadges = accountBadges;
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
