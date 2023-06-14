package com.westermeister.news.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * Spring Security configuration class.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
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
                    "/success"
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
            .sessionManagement(session -> session
                .maximumSessions(10)
            );
        return http.build();
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
