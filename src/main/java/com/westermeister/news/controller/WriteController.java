package com.westermeister.news.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.westermeister.news.entity.User;
import com.westermeister.news.form.SignUpForm;
import com.westermeister.news.form.UpdateEmailForm;
import com.westermeister.news.form.UpdateNameForm;
import com.westermeister.news.form.UpdatePasswordForm;
import com.westermeister.news.repository.SnippetRepository;
import com.westermeister.news.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * Handles all POST requests.
 */
@Controller
@RequestMapping("/api")
public class WriteController extends BaseController {
    /**
     * Don't allow more sign-ups than this.
     */
    private final int MAX_USERS = 1000;

    private Logger logger = LoggerFactory.getLogger(WriteController.class);

    private UserRepository userRepo;

    private PasswordEncoder passwordEncoder;

    /**
     * Inject dependencies.
     *
     * @param userRepo          data access object for the user table
     * @param passwordEncoder   used to hash and compare passwords
     * @param snippetRepo       used to initialize base class
     */
    public WriteController(UserRepository userRepo, PasswordEncoder passwordEncoder, SnippetRepository snippetRepo) {
        super(userRepo, snippetRepo);
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create a new user.
     *
     * @param signUpForm  form backing object
     * @param result      used to validate the form
     * @param redirect    used to add model attributes when redirecting
     * @param request     used to automatically sign in user after they sign up
     * @return            same page if any validation errors, otherwise account page
     */
    @PostMapping("/create/user")
    public String createUser(
        @Valid SignUpForm signUpForm,
        BindingResult result,
        RedirectAttributes redirect,
        HttpServletRequest request,
        Model model
    ) {
        String honeypot = signUpForm.getUsername();
        if (honeypot == null || honeypot.length() > 0) return "redirect:/success";
        if (userRepo.count() >= MAX_USERS) {
            redirect.addFlashAttribute("headerErrorMessage", "Sorry, we're not accepting more users at this time.");
            populateSignUpModel(model);
            return "signup";
        }
        if (result.hasErrors()) {
            populateSignUpModel(model);
            return "signup";
        }
        LocalDateTime signUpTime = LocalDateTime.now(ZoneOffset.UTC);
        User user = new User(
            signUpForm.getName(),
            signUpForm.getEmail(),
            passwordEncoder.encode(signUpForm.getPassword()),
            "ROLE_USER",
            signUpTime,
            signUpTime
        );
        try {
            userRepo.save(user);
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("email", null, "A user with this email address already exists.");
            populateSignUpModel(model);
            return "signup";
        }
        try {
            request.login(signUpForm.getEmail(), signUpForm.getPassword());
            redirect.addFlashAttribute(
                "headerSuccessMessage",
                "Thanks for signing up! You can view and make changes to your account below."
            );
            return "redirect:/account";
        } catch (ServletException e) {
            logger.error("Failed to sign in user after registration", e);
            return "redirect:/signin";
        }
    }

    /**
     * Update user's name.
     *
     * @param updateNameForm  form backing object
     * @param result          used to validate the form
     * @param redirect        used to add model attributes when redirecting
     * @param principal       currently signed-in user
     * @param request         used to sign out user if they're missing from database
     * @return                redirect to same page if success; no-redirect same page if validation errors;
     *                        and redirect to missing-user page if user is missing
     */
    @PostMapping("/update/user/name")
    public String updateUserName(
        @Valid UpdateNameForm updateNameForm,
        BindingResult result,
        RedirectAttributes redirect,
        Principal principal,
        HttpServletRequest request,
        Model model
    ) {
        if (result.hasErrors()) {
            String error = populateAccountModel(model, principal, request, redirect);
            if (!error.isEmpty()) return error;
            return "account";
        }
        User user = loadUserFromPrincipal(principal);
        if (user == null) return handleMissingUser(request, redirect, principal);
        user.setName(updateNameForm.getName());
        userRepo.save(user);
        redirect.addFlashAttribute("headerSuccessMessage", "Your name was successfully updated.");
        return "redirect:/account";
    }

    /**
     * Update user's email.
     *
     * @param updateEmailForm  form backing object
     * @param result           used to validate the form
     * @param redirect         used to add model attributes when redirecting
     * @param principal        currently signed-in user
     * @param request          used to sign out user if they're missing from the database
     * @return                 redirect to same page if success; no-redirect same page if validation errors;
     *                         and redirect to missing-user page if user is missing
     */
    @PostMapping("/update/user/email")
    public String updateUserEmail(
        @Valid UpdateEmailForm updateEmailForm,
        BindingResult result,
        RedirectAttributes redirect,
        Principal principal,
        HttpServletRequest request,
        Model model
    ) {
        if (result.hasErrors()) {
            String error = populateAccountModel(model, principal, request, redirect);
            if (!error.isEmpty()) return error;
            return "account";
        }
        User user = loadUserFromPrincipal(principal);
        if (user == null) return handleMissingUser(request, redirect, principal);
        user.setEmail(updateEmailForm.getEmail());
        try {
            userRepo.save(user);
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("email", null, "Another user is already using this email address.");
            String error = populateAccountModel(model, principal, request, redirect);
            if (!error.isEmpty()) return error;
            return "account";
        }
        redirect.addFlashAttribute("headerSuccessMessage", "Your email was successfully updated.");
        return "redirect:/account";
    }

    /**
     * Update user's password.
     *
     * @param updatePasswordForm  form backing object
     * @param result              used to validate the form
     * @param redirect            used to add model attributes when redirecting
     * @param principal           currently signed-in user
     * @param request             used to sign out user if they're missing from the database
     * @return                    redirect to same page if success; no-redirect same page if validation errors;
     *                            and redirect to missing-user page if user is missing
     */
    @PostMapping("/update/user/password")
    public String updateUserPassword(
        @Valid UpdatePasswordForm updatePasswordForm,
        BindingResult result,
        RedirectAttributes redirect,
        Principal principal,
        HttpServletRequest request,
        Model model
    ) {
        if (result.hasErrors()) {
            String error = populateAccountModel(model, principal, request, redirect);
            if (!error.isEmpty()) return error;
            return "account";
        }
        User user = loadUserFromPrincipal(principal);
        if (user == null) return handleMissingUser(request, redirect, principal);
        if (!passwordEncoder.matches(updatePasswordForm.getCurrentPassword(), user.getPassword())) {
            result.rejectValue("currentPassword", null, "Incorrect password.");
            String error = populateAccountModel(model, principal, request, redirect);
            if (!error.isEmpty()) return error;
            return "account";
        }
        String newPassword = passwordEncoder.encode(updatePasswordForm.getNewPassword());
        user.setPassword(newPassword);
        userRepo.save(user);
        redirect.addFlashAttribute("headerSuccessMessage", "Your password was successfully updated.");
        return "redirect:/account";
    }

    /**
     * Delete user's account.
     *
     * @param principal  current user
     * @param request    used to sign out the user after deletion
     * @param redirect   used to add model attributes when redirecting
     * @return           redirect to home page if success; redirect to missing-user page if user is missing;
     */
    @PostMapping("/delete/user")
    public String deleteUser(Principal principal, HttpServletRequest request, RedirectAttributes redirect) {
        User user = loadUserFromPrincipal(principal);
        if (user == null) return handleMissingUser(request, redirect, principal);
        try {
            request.logout();
        } catch (ServletException e) {
            logger.error(String.format("Failed to sign out user that's about to be deleted: %d%n", user.getId()), e);
        }
        userRepo.delete(user);
        redirect.addFlashAttribute("headerSuccessMessage", "Your account was succesfully deleted.");
        return "redirect:/";
    }
}
