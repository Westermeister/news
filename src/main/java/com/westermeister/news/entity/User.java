package com.westermeister.news.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "email", unique = true, length = 255)
    private String email;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "created")
    private Long created;

    @Column(name = "last_sign_in")
    private Long lastSignIn;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Session> sessions = new ArrayList<>();

    protected User() {}

    /**
     * Construct a new user.
     *
     * @param name        user's name; 255 chars max; can be "anything" e.g. full name, nickname, etc.
     * @param email       user's email address; 255 chars max; must be unique
     * @param password    should be an argon2id hash; required params listed here:
     *                    {@link com.westermeister.news.CryptoHelper#passwordHash(String)}
     * @param created     unix timestamp, precise to the second, for when the user signed up
     * @param lastSignIn  unix timestamp, precise to the second, for last sign-in (signing up counts as signing in)
     */
    public User(String name, String email, String password, Long created, Long lastSignIn) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.created = created;
        this.lastSignIn = lastSignIn;
    }

    /**
     * Construct human-readable string of the user entity.
     *
     * @return all of the user's columns formatted into a string
     */
    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", created="
                + created + ", lastSignIn=" + lastSignIn + "]";
    }

    /**
     * @see #User(String, String, String, Long, Long)
     *      documentation for each field
     */
    public Long getId() {
        return id;
    }

    /**
     * @see #User(String, String, String, Long, Long)
     *      documentation for each field
     */
    public String getName() {
        return name;
    }

    /**
     * @see #User(String, String, String, Long, Long)
     *      documentation for each field
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @see #User(String, String, String, Long, Long)
     *      documentation for each field
     */
    public String getEmail() {
        return email;
    }

    /**
     * @see #User(String, String, String, Long, Long)
     *      documentation for each field
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @see #User(String, String, String, Long, Long)
     *      documentation for each field
     */
    public String getPassword() {
        return password;
    }

    /**
     * @see #User(String, String, String, Long, Long)
     *      documentation for each field
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @see #User(String, String, String, Long, Long)
     *      documentation for each field
     */
    public Long getCreated() {
        return created;
    }

    /**
     * @see #User(String, String, String, Long, Long)
     *      documentation for each field
     */
    public void setCreated(Long created) {
        this.created = created;
    }

    /**
     * @see #User(String, String, String, Long, Long)
     *      documentation for each field
     */
    public Long getLastSignIn() {
        return lastSignIn;
    }

    /**
     * @see #User(String, String, String, Long, Long)
     *      documentation for each field
     */
    public void setLastSignIn(Long lastSignIn) {
        this.lastSignIn = lastSignIn;
    }
}
