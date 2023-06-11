package com.westermeister.news.form;

import org.hibernate.validator.constraints.CodePointLength;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

/**
 * Form for updating a user's name.
 *
 * @see com.westermeister.news.entity.User corresponding entity class
 */
public class UpdatePasswordForm {
    @NotNull
    @CodePointLength(min=8, message="Incorrect password.")
    @CodePointLength(max=255, message="Incorrect password.")
    private String currentPassword;

    @NotNull
    @CodePointLength(min=8, message="Enter at least 8 characters.")
    @CodePointLength(max=255, message="Shorten your password to 255 characters or less.")
    private String newPassword;

    @NotNull
    @CodePointLength(min=8, message="Enter at least 8 characters.")
    @CodePointLength(max=255, message="Shorten your password to 255 characters or less.")
    private String newPasswordAgain;

    @AssertTrue(message="New passwords do not match. Try typing them again.")
    public boolean isPasswordMatch() {
        return newPassword != null && newPasswordAgain != null && newPassword.equals(newPasswordAgain);
    }

    /**
     * Convert object to a human-readable string.
     *
     * @return all form fields
     */
    @Override
    public String toString() {
        return "UpdatePasswordForm [currentPassword=" + currentPassword + ", newPassword=" + newPassword
                + ", newPasswordAgain=" + newPasswordAgain + "]";
    }

    /**
     * @see com.westermeister.news.entity.User corresponding entity class
     */
    public String getCurrentPassword() {
        return currentPassword;
    }

    /**
     * @see com.westermeister.news.entity.User corresponding entity class
     */
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    /**
     * @see com.westermeister.news.entity.User corresponding entity class
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * @see com.westermeister.news.entity.User corresponding entity class
     */
    public void setNewPassword(String password) {
        this.newPassword = password;
    }

    /**
     * @see com.westermeister.news.entity.User corresponding entity class
     */
    public String getNewPasswordAgain() {
        return newPasswordAgain;
    }

    /**
     * @see com.westermeister.news.entity.User corresponding entity class
     */
    public void setNewPasswordAgain(String newPasswordAgain) {
        this.newPasswordAgain = newPasswordAgain;
    }
}
