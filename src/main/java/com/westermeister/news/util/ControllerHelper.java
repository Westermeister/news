package com.westermeister.news.util;

import java.security.Principal;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.westermeister.news.entity.User;
import com.westermeister.news.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Provides helpful utilities for controller logic.
 */
@Service
public class ControllerHelper {
    private UserRepository userRepo;

    /**
     * Inject dependencies.
     *
     * @param userRepo  used to access user table
     */
    public ControllerHelper(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Load user from database.
     *
     */
    public User loadUserFromPrincipal(Principal principal) {
        long userId = Long.parseLong(principal.getName());
        User user = userRepo.findById(userId).orElse(null);
        return user;
    }

    /**
     * If authenticated user is not found for some reason, sign them out.
     *
     * @param httpServletRequest  used to sign out the user
     * @param redirectAttributes  used to show an error message
     * @param principal           used to log user's id if they couldn't be signed out for some reason
     * @return                    sign-in page
     */
    public String handleMissingUser(
        HttpServletRequest httpServletRequest,
        RedirectAttributes redirectAttributes,
        Principal principal
    ) {
        try {
            httpServletRequest.logout();
        } catch (ServletException e) {
            long userId = Long.parseLong(principal.getName());
            System.err.format("Failed to sign out user with unknown ID: %d%n", userId);
        }
        redirectAttributes.addFlashAttribute("headerErrorMessage", "Please sign in again.");
        return "redirect:/signin";
    }
}
