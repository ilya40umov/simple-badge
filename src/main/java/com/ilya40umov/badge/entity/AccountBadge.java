package com.ilya40umov.badge.entity;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;

/**
 * Represents the fact of awarding a badge to a certain user.
 *
 * @author isorokoumov
 */
@Entity
@Table(name = "account_badge",
        indexes = {@Index(name = "account_badge_by_account_id_badge_id",
                columnList = "account_id, badge_id", unique = true)})
public class AccountBadge {

    private Long accountBadgeId;

    private String comment;

    private Instant created;

    private Account account;
    private Badge badge;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_badge_id", updatable = false, nullable = false)
    public Long getAccountBadgeId() {
        return accountBadgeId;
    }

    public AccountBadge setAccountBadgeId(Long accountBadgeId) {
        this.accountBadgeId = accountBadgeId;
        return this;
    }

    @Column(name = "comment", length = 512)
    public String getComment() {
        return comment;
    }

    public AccountBadge setComment(String comment) {
        this.comment = comment;
        return this;
    }

    @CreatedDate
    @Column(name = "created", columnDefinition = "datetime(3)", nullable = false, updatable = false)
    public Instant getCreated() {
        return created;
    }

    public AccountBadge setCreated(Instant created) {
        this.created = created;
        return this;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_account_badge_account_id"))
    public Account getAccount() {
        return account;
    }

    public AccountBadge setAccount(Account account) {
        this.account = account;
        return this;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "badge_id", nullable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_account_badge_badge_id"))
    public Badge getBadge() {
        return badge;
    }

    public AccountBadge setBadge(Badge badge) {
        this.badge = badge;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountBadge)) return false;

        AccountBadge that = (AccountBadge) o;

        if (getAccount() != null ? !getAccount().equals(that.getAccount()) : that.getAccount() !=
                null)
            return false;
        return getBadge() != null ? getBadge().equals(that.getBadge()) : that.getBadge() == null;
    }

    @Override
    public int hashCode() {
        int result = getAccount() != null ? getAccount().hashCode() : 0;
        result = 31 * result + (getBadge() != null ? getBadge().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AccountBadge{" +
                "accountBadgeId=" + accountBadgeId +
                ", comment='" + comment + '\'' +
                ", created=" + created +
                ", accountId=" + account.getAccountId() +
                ", badgeId=" + badge.getBadgeId() +
                '}';
    }
}
