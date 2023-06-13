package com.westermeister.news.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.westermeister.news.entity.User;
import com.westermeister.news.repository.UserRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepo;

    /**
     * Construct new object with dependency injection.
     *
     * @param userRepo  user repository
     */
    public CustomUserDetailsService(UserRepository userRepo) {
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
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole()));

        // Below, we use the user ID, instead of the email, for Spring Security's notion of a "username".
        // This is because we want to allow the user to change their email without requiring re-authentication.
        return new org.springframework.security.core.userdetails.User(
            user.getId().toString(),
            password,
            roles
        );
    }
}
