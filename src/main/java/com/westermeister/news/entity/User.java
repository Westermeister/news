package com.westermeister.news.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity class for users.
 */
@Entity
@Table(name = "user_")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "created")
    private Long created;

    @Column(name = "last_sign_in")
    private Long lastSignIn;

    protected User() {}

    /**
     * Construct a new user.
     *
     * @see "User.md" for schema documentation
     */
    public User(String name, String email, String password, Long created, Long lastSignIn) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.created = created;
        this.lastSignIn = lastSignIn;
    }

    /**
     * Convert object to a human-readable string.
     *
     * @return all of the user's fields
     */
    @Override
    public String toString() {
        return String.format(
            "User [id=%d, name=%s, email=%s, password=%s, created=%d, last_sign_in=%d]",
            id, name, email, password, created, lastSignIn
        );
    }

    /**
     * @see "User.md" for schema documentation
     */
    public Long getId() {
        return id;
    }

    /**
     * @see "User.md" for schema documentation
     */
    public String getName() {
        return name;
    }

    /**
     * @see "User.md" for schema documentation
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @see "User.md" for schema documentation
     */
    public String getEmail() {
        return email;
    }

    /**
     * @see "User.md" for schema documentation
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @see "User.md" for schema documentation
     */
    public String getPassword() {
        return password;
    }

    /**
     * @see "User.md" for schema documentation
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @see "User.md" for schema documentation
     */
    public Long getCreated() {
        return created;
    }

    /**
     * @see "User.md" for schema documentation
     */
    public void setCreated(Long created) {
        this.created = created;
    }

    /**
     * @see "User.md" for schema documentation
     */
    public Long getLastSignIn() {
        return lastSignIn;
    }

    /**
     * @see "User.md" for schema documentation
     */
    public void setLastSignIn(Long lastSignIn) {
        this.lastSignIn = lastSignIn;
    }
}
