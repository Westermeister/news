package com.westermeister.news.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
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
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_session_user_id_user_"))
    private User user;

    @Column(name = "token", length = 255)
    private String token;

    protected Session() {}

    /**
     * Construct a new session.
     *
     * @param user   the user associated with this session
     * @param token  a SHA3-512 hash (base64url w/o padding) of a random 512-bit number
     */
    public Session(User user, String token) {
        this.user = user;
        this.token = token;
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
}
