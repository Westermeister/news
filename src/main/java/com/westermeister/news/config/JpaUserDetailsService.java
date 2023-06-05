package com.westermeister.news.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.westermeister.news.entity.User;
import com.westermeister.news.repository.UserRepository;

@Component
public class JpaUserDetailsService implements UserDetailsService {
    private UserRepository userRepo;

    /**
     * Construct new object with dependency injection.
     *
     * @param userRepo  user repository
     */
    public JpaUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Load user for Spring Security.
     *
     * @param username  in our case, this is actually the user's email address
     * @return          user metadata
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String email = username;
        List<User> users = userRepo.findFirstByEmail(email);
        if (users.isEmpty()) {
            String message = String.format("User not found: %s", email);
            throw new UsernameNotFoundException(message);
        }
        User user = users.get(0);
        String password = user.getPassword();
        return new org.springframework.security.core.userdetails.User(email, password, new ArrayList<>());
    }
}
