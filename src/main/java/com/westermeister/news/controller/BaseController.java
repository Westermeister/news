package com.westermeister.news.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.westermeister.news.entity.Snippet;
import com.westermeister.news.entity.User;
import com.westermeister.news.form.SignUpForm;
import com.westermeister.news.form.UpdateEmailForm;
import com.westermeister.news.form.UpdateNameForm;
import com.westermeister.news.form.UpdatePasswordForm;
import com.westermeister.news.repository.SnippetRepository;
import com.westermeister.news.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Provide common methods shared by all controllers.
 */
public abstract class BaseController {
    private Logger logger = LoggerFactory.getLogger(BaseController.class);
    private UserRepository userRepo;
    private SnippetRepository snippetRepo;

    /**
     * Inject dependencies.
     *
     * @param userRepo     used to access users
     * @param snippetRepo  used to access news snippets
     */
    public BaseController(UserRepository userRepo, SnippetRepository snippetRepo) {
        this.userRepo = userRepo;
        this.snippetRepo = snippetRepo;
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
     * @param request    used to sign out the user
     * @param redirect   used to show an error message
     * @param principal  used to log user's id if they couldn't be signed out for some reason
     * @return           sign-in page
     */
    public String handleMissingUser(
        HttpServletRequest request,
        RedirectAttributes redirect,
        Principal principal
    ) {
        try {
            request.logout();
        } catch (ServletException e) {
            long userId = Long.parseLong(principal.getName());
            logger.error(String.format("Failed to sign out user with unknown ID: %d%n", userId));
        }
        redirect.addFlashAttribute("headerErrorMessage", "Please sign in again.");
        return "redirect:/signin";
    }

    /**
     * Populates the model for the homepage.
     *
     * @param model  will be modified to add the news snippets
     */
    public void populateIndexModel(Model model) {
        List<Snippet> snippets = snippetRepo.findAllByOrderBySlotAsc();
        model.addAttribute("snippets", snippets);
    }

    /**
     * Populates the model for the sign-up page.
     *
     * @param model  will be modified to add the sign-up form
     */
    public void populateSignUpModel(Model model) {
        if (!model.containsAttribute("signUpForm")) {
            model.addAttribute("signUpForm", new SignUpForm());
        }
    }

    /**
     * Populates the model for the account page.
     *
     * @param model      will be modified to add the relevant attributes
     * @param request    used to handle missing user
     * @param redirect   used to handle missing user
     * @param principal  used to handle missing user
     * @return           empty string if no missing user, otherwise template name to render
     */
    public String populateAccountModel(
        Model model,
        Principal principal,
        HttpServletRequest request,
        RedirectAttributes redirect
    ) {
        if (!model.containsAttribute("currentName") || !model.containsAttribute("currentEmail")) {
            User user = loadUserFromPrincipal(principal);
            if (user == null) {
                return handleMissingUser(request, redirect, principal);
            }
            model.addAttribute("currentName", user.getName());
            model.addAttribute("currentEmail", user.getEmail());
        }
        if (!model.containsAttribute("updateNameForm")) {
            model.addAttribute("updateNameForm", new UpdateNameForm());
        }
        if (!model.containsAttribute("updateEmailForm")) {
            model.addAttribute("updateEmailForm", new UpdateEmailForm());
        }
        if (!model.containsAttribute("updatePasswordForm")) {
            model.addAttribute("updatePasswordForm", new UpdatePasswordForm());
        }
        return "";
    }
}
