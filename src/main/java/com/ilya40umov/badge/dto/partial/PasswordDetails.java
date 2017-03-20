package com.ilya40umov.badge.dto.partial;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Contains data required to perform password update.
 *
 * @author isorokoumov
 */
public class PasswordDetails {

    private String currentPassword;

    @Size(min = 6, max = 32)
    @Pattern(regexp = "[a-zA-Z0-9$@!%*#?&]{6,32}")
    private String newPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public PasswordDetails setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
        return this;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public PasswordDetails setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }

    @Override
    public String toString() {
        return "PasswordDetails{" +
                "currentPassword='" + currentPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
