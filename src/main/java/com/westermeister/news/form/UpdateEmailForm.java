package com.westermeister.news.form;

import org.hibernate.validator.constraints.CodePointLength;

import jakarta.validation.constraints.NotNull;

/**
 * Form for updating a user's email.
 *
 * @see com.westermeister.news.entity.User corresponding entity class
 */
public class UpdateEmailForm {
    @NotNull
    @CodePointLength(min=1, message="Enter your new email address.")
    @CodePointLength(max=255, message="Provide an email address that is 255 characters or less.")
    private String email;

    /**
     * Convert object to a human-readable string.
     *
     * @return the form fields
     */
    @Override
    public String toString() {
        return "UpdateEmailForm [email=" + email + "]";
    }

    /**
     * @see com.westermeister.news.entity.User corresponding entity class
     */
    public String getEmail() {
        return email;
    }

    /**
     * @see com.westermeister.news.entity.User corresponding entity class
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
