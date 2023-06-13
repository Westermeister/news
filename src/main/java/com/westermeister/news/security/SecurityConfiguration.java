package com.westermeister.news.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
                .requestMatchers(HttpMethod.POST, "/api/user").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/signin")
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutUrl("/signout")
                .permitAll()
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
}
