package com.westermeister.news.entity;

import java.time.LocalDateTime;

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

    @Column(name = "role")
    private String role;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "last_sign_in")
    private LocalDateTime lastSignIn;

    protected User() {}

    /**
     * Construct a new user.
     *
     * @see "User.md" for schema documentation
     */
    public User(
        String name,
        String email,
        String password,
        String role,
        LocalDateTime created,
        LocalDateTime lastSignIn
    ) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
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
        return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
                + ", created=" + created + ", lastSignIn=" + lastSignIn + "]";
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
    public String getRole() {
        return role;
    }

    /**
     * @see "User.md" for schema documentation
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @see "User.md" for schema documentation
     */
    public LocalDateTime getCreated() {
        return created;
    }

    /**
     * @see "User.md" for schema documentation
     */
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    /**
     * @see "User.md" for schema documentation
     */
    public LocalDateTime getLastSignIn() {
        return lastSignIn;
    }

    /**
     * @see "User.md" for schema documentation
     */
    public void setLastSignIn(LocalDateTime lastSignIn) {
        this.lastSignIn = lastSignIn;
    }
}
