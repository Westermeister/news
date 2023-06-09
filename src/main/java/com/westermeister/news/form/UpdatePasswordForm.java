package com.westermeister.news.form;

import org.hibernate.validator.constraints.CodePointLength;

import jakarta.validation.constraints.NotNull;

/**
 * Form for updating a user's name.
 *
 * @see com.westermeister.news.entity.User corresponding entity class
 */
public class UpdatePasswordForm {
    @NotNull
    @CodePointLength(min=1, message="Enter your current password.")
    @CodePointLength(max=255, message="All existing passwords are 255 characters or less. Please try again.")
    private String currentPassword;

    @NotNull
    @CodePointLength(min=8, message="Enter a valid password.")
    @CodePointLength(max=255, message="Shorten your password to 255 characters or less.")
    private String newPassword;

    @NotNull
    private String newPasswordAgain;

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
