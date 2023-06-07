package com.westermeister.news.controller;

import java.security.Principal;
import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.westermeister.news.entity.User;
import com.westermeister.news.form.SignUpForm;
import com.westermeister.news.form.UpdateEmailForm;
import com.westermeister.news.form.UpdateNameForm;
import com.westermeister.news.repository.UserRepository;
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

    /**
     * Inject dependencies.
     *
     * @param userRepo      data access object for the user table
     * @param cryptoHelper  helper class with cryptographic utilities
     */
    public WriteController(UserRepository userRepo, CryptoHelper cryptoHelper) {
        this.userRepo = userRepo;
        this.cryptoHelper = cryptoHelper;
    }

    /**
     * Create a new user.
     *
     * @param signUpForm          form object for validation and transfer of form data
     * @param bindingResult       used to validate the form
     * @param redirectAttributes  used to add model attributes to redirected routes
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
                bindingResult.rejectValue("email", null, "A user with this email already exists.");
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
            creationTime
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
            return "redirect:/signin";
        }
    }

    /**
     * Update user's name.
     *
     * @param principal           currently signed-in user
     * @param updateNameForm      form data transfer object
     * @param bindingResult       used to validate the form
     * @param redirectAttributes  used to add model attributes to redirected routes
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
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.updateNameForm",
                bindingResult
            );
            redirectAttributes.addFlashAttribute("updateNameForm", updateNameForm);
            redirectAttributes.addFlashAttribute(
                "headerErrorMessage",
                "Your name was not updated. See the error below."
            );
            return "redirect:/account";
        }

        List<User> users = userRepo.findFirstByEmail(principal.getName());
        if (users.isEmpty()) {
            try {
                httpServletRequest.logout();
            } catch (ServletException e) {
                System.err.format("Failed to sign out user with unknown email: %s", principal.getName());
            }
            redirectAttributes.addFlashAttribute("headerErrorMessage", "Please sign in again.");
            return "redirect:/signin";
        }
        User user = users.get(0);
        user.setName(updateNameForm.getName());
        userRepo.save(user);

        redirectAttributes.addFlashAttribute("headerSuccessMessage", "Your name was successfully updated.");
        return "redirect:/account";
    }

    /**
     * Update user's email.
     *
     * @param principal
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
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.updateEmailForm",
                bindingResult
            );
            redirectAttributes.addFlashAttribute("updateEmailForm", updateEmailForm);
            redirectAttributes.addFlashAttribute(
                "headerErrorMessage",
                "Your email was not updated. See the error below."
            );
            return "redirect:/account";
        }

        List<User> users = userRepo.findFirstByEmail(principal.getName());
        if (users.isEmpty()) {
            try {
                httpServletRequest.logout();
            } catch (ServletException e) {
                System.err.format("Failed to sign out user with unknown email: %s", principal.getName());
            }
            redirectAttributes.addFlashAttribute("headerErrorMessage", "Please sign in again.");
            return "redirect:/signin";
        }
        User user = users.get(0);
        user.setEmail(updateEmailForm.getEmail());
        userRepo.save(user);

        try {
            httpServletRequest.logout();
            redirectAttributes.addFlashAttribute(
                "headerSuccessMessage",
                "Your email was successfully updated. Please sign in again."
            );
            return "redirect:/signin";
        } catch (ServletException e) {
            return "redirect:/signin";
        }
    }
}
