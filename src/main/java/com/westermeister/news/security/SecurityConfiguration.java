package com.westermeister.news.security;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.westermeister.news.entity.User;
import com.westermeister.news.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Spring Security configuration class.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private UserRepository userRepo;
    private HttpServletRequest request;

    /**
     * Inject dependencies.
     *
     * @param userRepo  used to load user details
     */
    public SecurityConfiguration(UserRepository userRepo, HttpServletRequest request) {
        this.userRepo = userRepo;
        this.request = request;
    }

    /**
     * Define list of security filters before allowing a request through.
     *
     * @param http  configuration object
     * @return      resulting security configuration
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers(
                    HttpMethod.GET,
                    "/css/**",
                    "/img/**",
                    "/js/**",
                    "/",
                    "/signup",
                    "/signin",
                    "/success",
                    "/terms-of-service",
                    "/privacy-policy"
                ).permitAll()
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/create/user"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/signin")
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutUrl("/signout")
                .permitAll()
            )
            .sessionManagement((session) -> session
                .maximumSessions(5)
            );
        return http.build();
    }

    @Bean
    UserDetailsService userDetailsService() {
        return (username) -> {
            if (request.getRequestURI().equals("/signin")) {
                // Throttle authentication to mitigate brute force attacks.
                // On average, signing in will take 4 to 5 seconds.
                int randomDelay = ThreadLocalRandom.current().nextInt(3, 6);
                try {
                    TimeUnit.SECONDS.sleep(randomDelay);
                } catch (InterruptedException e) {
                    System.err.format("Couldn't sleep because of error: %s%n", e.toString());
                }
            }

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
            return new org.springframework.security.core.userdetails.User(user.getId().toString(), password, roles);
        };
    }

    /**
     * Define password hashing function.
     *
     * @return argon2id with spring security default params for 5.8
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    /**
     * Define session event publisher to limit concurrent user sessions.
     * <p>
     * This is required in order to add the ".sessionManagement(...)"
     * line to the security filter chain configuration.
     *
     * @return session event publisher
     */
    @Bean
    HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
