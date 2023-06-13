package com.westermeister.news.form;

import org.hibernate.validator.constraints.CodePointLength;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

/**
 * Form for creating new users.
 *
 * @see com.westermeister.news.entity.User corresponding entity class
 */
public class SignUpForm {
    // This is a honeypot field.
    private String username;

    @NotNull(message="This field is required.")
    @CodePointLength(min=1, message="Enter your full name.")
    @CodePointLength(max=255, message="Shorten your name to 255 characters or less, or just use your first name.")
    private String name;

    @NotNull(message="This field is required.")
    @CodePointLength(min=1, message="Enter your email address.")
    @CodePointLength(max=255, message="Provide an email address that is 255 characters or less.")
    private String email;

    @NotNull(message="This field is required.")
    @CodePointLength(min=8, message="Enter at least 8 characters.")
    @CodePointLength(max=255, message="Shorten your password to 255 characters or less.")
    private String password;

    @NotNull(message="This field is required.")
    @CodePointLength(min=8, message="Enter at least 8 characters.")
    @CodePointLength(max=255, message="Shorten your password to 255 characters or less.")
    private String passwordAgain;

    @AssertTrue(message="Passwords do not match. Try typing them again.")
    public boolean isPasswordMatch() {
        return password != null && passwordAgain != null && password.equals(passwordAgain);
    }

    /**
     * Convert object into a human-readable string.
     *
     * @return the form fields
     */
    @Override
    public String toString() {
        return "SignUpForm [username=" + username + ", name=" + name + ", email=" + email + ", password=" + password
                + ", passwordAgain=" + passwordAgain + "]";
    }

    /**
     * Get honeypot field.
     *
     * @return the "username", which should not be filled out
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set honeypot field.
     *
     * @param username  value to set for the honeypot field
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @see com.westermeister.news.entity.User corresponding entity class
     */
    public String getName() {
        return name;
    }

    /**
     * @see com.westermeister.news.entity.User corresponding entity class
     */
    public void setName(String name) {
        this.name = name;
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

    /**
     * @see com.westermeister.news.entity.User corresponding entity class
     */
    public String getPassword() {
        return password;
    }

    /**
     * @see com.westermeister.news.entity.User corresponding entity class
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @see com.westermeister.news.entity.User corresponding entity class
     */
    public String getPasswordAgain() {
        return passwordAgain;
    }

    /**
     * @see com.westermeister.news.entity.User corresponding entity class
     */
    public void setPasswordAgain(String passwordAgain) {
        this.passwordAgain = passwordAgain;
    }
}
