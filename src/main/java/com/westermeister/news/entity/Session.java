package com.westermeister.news.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entity class for browser sessions.
 */
@Entity
@Table(name = "session")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "token")
    private String token;

    @Column(name = "created")
    private Long created;

    @Column(name = "expires")
    private Long expires;

    protected Session() {}

    /**
     * Construct a new session.
     *
     * @param user     the user associated with this session
     * @param token    a SHA3-512 hash (base64url w/o padding) of a random 512-bit number
     * @param created  unix timestamp for when session was created; second precision
     * @param expires  unix timestamp for when session will expire; second precision
     */
    public Session(User user, String token, Long created, Long expires) {
        this.user = user;
        this.token = token;
        this.created = created;
        this.expires = expires;
    }

    /**
     * Construct human-readable string of the session entity.
     *
     * @return all of the session's columns formatted into a string
     */
    @Override
    public String toString() {
        return String.format(
            "Session [id=%d, user=%d, token=%s, created=%d, expires=%d]",
            id, user.getId(), token, created, expires
        );
    }

    /**
     * @see #Session(User, String)
     *      documentation for each field
     */
    public Long getId() {
        return id;
    }

    /**
     * @see #Session(User, String)
     *      documentation for each field
     */
    public User getUser() {
        return user;
    }

    /**
     * @see #Session(User, String)
     *      documentation for each field
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @see #Session(User, String)
     *      documentation for each field
     */
    public String getToken() {
        return token;
    }

    /**
     * @see #Session(User, String)
     *      documentation for each field
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @see #Session(User, String)
     *      documentation for each field
     */
    public Long getCreated() {
        return created;
    }

    /**
     * @see #Session(User, String)
     *      documentation for each field
     */
    public void setCreated(Long created) {
        this.created = created;
    }

    /**
     * @see #Session(User, String)
     *      documentation for each field
     */
    public Long getExpires() {
        return expires;
    }

    /**
     * @see #Session(User, String)
     *      documentation for each field
     */
    public void setExpires(Long expires) {
        this.expires = expires;
    }
}
