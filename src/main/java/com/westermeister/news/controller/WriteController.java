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
        HttpServletRequest request,
        @Valid SignUpForm signUpForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
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
            request.login(signUpForm.getEmail(), signUpForm.getPassword());
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
    @PatchMapping("/api/user")
    public String updateUserName(
        Principal principal,
        @Valid UpdateNameForm updateNameForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.updateNameForm",
                bindingResult
            );
            redirectAttributes.addFlashAttribute(
                "headerErrorMessage",
                "Your name was not updated. See the error below."
            );
            redirectAttributes.addFlashAttribute("updateNameForm", updateNameForm);
            return "redirect:/account";
        }

        List<User> users = userRepo.findFirstByEmail(principal.getName());
        if (users.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                "headerErrorMessage",
                "We couldn't find your account. It may have been deleted by accident."
            );
            return "redirect:/signup";
        }
        User user = users.get(0);
        user.setName(updateNameForm.getName());
        userRepo.save(user);

        redirectAttributes.addFlashAttribute("headerSuccessMessage", "Your name has been successfully updated!");
        return "redirect:/account";
    }
}
