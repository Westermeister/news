package com.westermeister.news.form;

import org.hibernate.validator.constraints.CodePointLength;

import jakarta.validation.constraints.NotNull;

/**
 * Form for updating a user's name.
 *
 * @see com.westermeister.news.entity.User corresponding entity class
 */
public class UpdateNameForm {
    @NotNull
    @CodePointLength(min=1, message="Enter your new name.")
    @CodePointLength(max=255, message="Shorten your name to 255 characters or less, or just use your first name.")
    private String name;

    /**
     * Convert object into a human-readable string.
     *
     * @return the form fields
     */
    @Override
    public String toString() {
        return "UpdateNameForm [name=" + name + "]";
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
}
