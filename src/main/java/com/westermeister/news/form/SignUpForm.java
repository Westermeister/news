package com.westermeister.news.form;

import org.hibernate.validator.constraints.CodePointLength;

import jakarta.validation.constraints.NotNull;

/**
 * Form for creating new users.
 *
 * @see com.westermeister.news.entity.User corresponding entity class
 */
public class SignUpForm {
    @NotNull
    @CodePointLength(min=1, message="Enter your full name.")
    @CodePointLength(max=255, message="Shorten your name to 255 characters or less, or just use your first name.")
    private String name;

    @NotNull
    @CodePointLength(min=1, message="Enter your email address.")
    @CodePointLength(max=255, message="Provide an email that is 255 characters or less.")
    private String email;

    @NotNull
    @CodePointLength(min=8, message="Enter at least 8 characters.")
    @CodePointLength(max=255, message="Shorten your password to 255 characters or less.")
    private String password;

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
}
