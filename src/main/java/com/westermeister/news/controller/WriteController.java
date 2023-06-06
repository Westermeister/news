package com.westermeister.news.controller;

import java.time.Instant;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.westermeister.news.entity.User;
import com.westermeister.news.form.SignUpForm;
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
            redirectAttributes.addFlashAttribute("currentName", signUpForm.getName());
            redirectAttributes.addFlashAttribute("currentEmail", signUpForm.getEmail());
            return "redirect:/account";
        } catch (ServletException e) {
            return "redirect:/signin";
        }
    }

        return "redirect:/account";
    }
}
