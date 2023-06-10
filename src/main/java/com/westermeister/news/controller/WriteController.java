package com.westermeister.news.controller;

import java.security.Principal;
import java.time.Instant;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.westermeister.news.entity.User;
import com.westermeister.news.form.SignUpForm;
import com.westermeister.news.form.UpdateEmailForm;
import com.westermeister.news.form.UpdateNameForm;
import com.westermeister.news.form.UpdatePasswordForm;
import com.westermeister.news.repository.UserRepository;
import com.westermeister.news.util.ControllerHelper;
import com.westermeister.news.util.CryptoHelper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * Handles all non-GET requests i.e. POST, PUT, and DELETE.
 */
@Controller
public class WriteController {
    private UserRepository userRepo;
    private CryptoHelper cryptoHelper;
    private ControllerHelper controllerHelper;

    /**
     * Inject dependencies.
     *
     * @param userRepo          data access object for the user table
     * @param cryptoHelper      helper class with cryptographic utilities
     * @param controllerHelper  contains helpful utilities for controller logic
     */
    public WriteController(UserRepository userRepo, CryptoHelper cryptoHelper, ControllerHelper controllerHelper) {
        this.userRepo = userRepo;
        this.cryptoHelper = cryptoHelper;
        this.controllerHelper = controllerHelper;
    }

    /**
     * Create a new user.
     *
     * @param signUpForm          form object for validation and transfer of form data
     * @param bindingResult       used to validate the form
     * @param redirectAttributes  used to add model attributes to redirected routes
     * @param httpServletRequest  used to automatically sign in user after they sign up
     * @return                    same page if any validation errors, otherwise account page
     */
    @PostMapping("/api/user")
    public String createUser(
        @Valid SignUpForm signUpForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        HttpServletRequest httpServletRequest
    ) {
        boolean containsBasicErrors = bindingResult.hasErrors();
        boolean passwordsDontMatch = !signUpForm.getPassword().equals(signUpForm.getPasswordAgain());
        boolean userAlreadyExists = !userRepo.findFirstByEmail(signUpForm.getEmail()).isEmpty();
        if (containsBasicErrors || passwordsDontMatch || userAlreadyExists) {
            if (passwordsDontMatch) {
                bindingResult.rejectValue("password", null, "The passwords didn't match. Try typing them again.");
                bindingResult.rejectValue("passwordAgain", null, "The passwords didn't match. Try typing them again.");
            }
            if (userAlreadyExists) {
                bindingResult.rejectValue("email", null, "A user with this email address already exists.");
            }
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.signUpForm",
                bindingResult
            );
            redirectAttributes.addFlashAttribute("signUpForm", signUpForm);
            return "redirect:/signup";
        }

        long creationTime = Instant.now().getEpochSecond();
        User user = new User(
            signUpForm.getName(),
            signUpForm.getEmail(),
            cryptoHelper.passwordHash(signUpForm.getPassword()),
            creationTime,
            creationTime,
            "ROLE_USER"
        );
        userRepo.save(user);

        try {
            httpServletRequest.login(signUpForm.getEmail(), signUpForm.getPassword());
            redirectAttributes.addFlashAttribute(
                "headerSuccessMessage",
                "Thanks for signing up! You can view and make changes to your account below."
            );
            return "redirect:/account";
        } catch (ServletException e) {
            System.err.println("Failed to sign in user after registration.");
            return "redirect:/signin";
        }
    }

    /**
     * Update user's name.
     *
     * @param updateNameForm      form data transfer object
     * @param bindingResult       used to validate the form
     * @param redirectAttributes  used to add model attributes to redirected routes
     * @param principal           currently signed-in user
     * @param httpServletRequest  used to sign out user
     * @return                    same page, optionally including validation errors, if any
     */
    @PatchMapping("/api/user/name")
    public String updateUserName(
        @Valid UpdateNameForm updateNameForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Principal principal,
        HttpServletRequest httpServletRequest
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("updateNameForm", updateNameForm);
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.updateNameForm",
                bindingResult
            );
            redirectAttributes.addFlashAttribute(
                "headerErrorMessage",
                "Your name was not updated. See the error below."
            );
            return "redirect:/account";
        }

        User user = controllerHelper.loadUserFromPrincipal(principal);
        if (user == null) {
            return controllerHelper.handleMissingUser(httpServletRequest, redirectAttributes, principal);
        }

        user.setName(updateNameForm.getName());
        userRepo.save(user);
        redirectAttributes.addFlashAttribute("headerSuccessMessage", "Your name was successfully updated.");
        return "redirect:/account";
    }

    /**
     * Update user's email.
     *
     * @param updateEmailForm     form data transfer object
     * @param bindingResult       used to validate the form
     * @param redirectAttributes  used to add model attributes to redirected routes
     * @param principal           currently signed-in user
     * @param httpServletRequest  used to sign out user
     * @return                    same page, optionally including validation errors, if any
     */
    @PatchMapping("/api/user/email")
    public String updateUserEmail(
        @Valid UpdateEmailForm updateEmailForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Principal principal,
        HttpServletRequest httpServletRequest
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("updateEmailForm", updateEmailForm);
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.updateEmailForm",
                bindingResult
            );
            redirectAttributes.addFlashAttribute(
                "headerErrorMessage",
                "Your email was not updated. See the error below."
            );
            return "redirect:/account";
        }

        User user = controllerHelper.loadUserFromPrincipal(principal);
        if (user == null) {
            return controllerHelper.handleMissingUser(httpServletRequest, redirectAttributes, principal);
        }

        user.setEmail(updateEmailForm.getEmail());
        try {
            userRepo.save(user);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("updateEmailForm", updateEmailForm);
            bindingResult.rejectValue("email", null, "Another user is already using this email address.");
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.updateEmailForm",
                bindingResult
            );
            redirectAttributes.addFlashAttribute(
                "headerErrorMessage",
                "Your email was not updated. See the error below."
            );
            return "redirect:/account";
        }

        redirectAttributes.addFlashAttribute("headerSuccessMessage", "Your email was successfully updated.");
        return "redirect:/account";
    }

    /**
     * Update user's password.
     *
     * @param updatePasswordForm  form data transfer object
     * @param bindingResult       used to validate the form
     * @param redirectAttributes  used to add model attributes to redirected routes
     * @param principal           currently signed-in user
     * @param httpServletRequest  used to sign out user
     * @return                    same page, optionally including validation errors, if any
     */
    @PatchMapping("/api/user/password")
    public String updateUserPassword(
        @Valid UpdatePasswordForm updatePasswordForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Principal principal,
        HttpServletRequest httpServletRequest
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("updatePasswordForm", updatePasswordForm);
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.updatePasswordForm",
                bindingResult
            );
            redirectAttributes.addFlashAttribute(
                "headerErrorMessage",
                "Your password was not updated. See the error below."
            );
            return "redirect:/account";
        }

        User user = controllerHelper.loadUserFromPrincipal(principal);
        if (user == null) {
            return controllerHelper.handleMissingUser(httpServletRequest, redirectAttributes, principal);
        }

        if (!cryptoHelper.verifyPasswordHash(updatePasswordForm.getCurrentPassword(), user.getPassword())) {
            redirectAttributes.addFlashAttribute("updatePasswordForm", updatePasswordForm);
            bindingResult.rejectValue("currentPassword", null, "Incorrect password.");
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.updatePasswordForm",
                bindingResult
            );
            redirectAttributes.addFlashAttribute(
                "headerErrorMessage",
                "Your password was not updated. See the error below."
            );
            return "redirect:/account";
        }

        String newPassword = cryptoHelper.passwordHash(updatePasswordForm.getNewPassword());
        user.setPassword(newPassword);
        userRepo.save(user);
        redirectAttributes.addFlashAttribute("headerSuccessMessage", "Your password was successfully updated.");
        return "redirect:/account";
    }
}
