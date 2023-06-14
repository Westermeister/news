package com.westermeister.news.security;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.westermeister.news.entity.User;
import com.westermeister.news.repository.UserRepository;

/**
 * Listens for security-related events.
 */
@Component
public class SecurityListener {
    private UserRepository userRepo;

    /**
     * Inject dependencies.
     *
     * @param userRepo  used to access user table
     */
    public SecurityListener(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Keep track of user sign-ins.
     *
     * @param event  successful authentication event
     */
    @EventListener
    public void trackUserSignIn(AuthenticationSuccessEvent event) {
        String principal = event.getAuthentication().getName();
        long userId = Long.parseLong(principal);
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            return;
        }

        LocalDateTime lastSignIn = LocalDateTime.now(ZoneOffset.UTC);
        user.setLastSignIn(lastSignIn);
        userRepo.save(user);
    }
}
