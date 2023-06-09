package com.westermeister.news.util;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Provides helpful utilities for controller logic.
 */
@Service
public class ControllerHelper {
    /**
     * If authenticated user is not found for some reason, sign them out.
     *
     * @param httpServletRequest  used to sign out the user
     * @param redirectAttributes  used to show an error message
     * @param userId              used to log user's id if they couldn't be signed out for some reason
     * @return                    sign-in page
     */
    public String handleMissingUser(
        HttpServletRequest httpServletRequest,
        RedirectAttributes redirectAttributes,
        long userId
    ) {
        try {
            httpServletRequest.logout();
        } catch (ServletException e) {
            System.err.format("Failed to sign out user with unknown ID: %d%n", userId);
        }
        redirectAttributes.addFlashAttribute("headerErrorMessage", "Please sign in again.");
        return "redirect:/signin";
    }
}
