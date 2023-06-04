package com.westermeister.news.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.westermeister.news.entity.User;
import com.westermeister.news.form.SignUpForm;
import com.westermeister.news.repository.UserRepository;
import com.westermeister.news.util.CryptoHelper;

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
     * @param model  context for rendering
     * @return       account page
     */
    @PostMapping("/api/user")
    public String createUser(
        @Valid SignUpForm signUpForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.signUpForm",
                bindingResult
            );
            redirectAttributes.addFlashAttribute("signUpForm", signUpForm);
            return "redirect:/signup";
        }

        List<User> userWithSameEmail = userRepo.findFirstByEmail(signUpForm.getEmail());
        if (!userWithSameEmail.isEmpty()) {
            redirectAttributes.addFlashAttribute("user_already_exists", "A user with that email already exists.");
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
        redirectAttributes.addFlashAttribute("name", signUpForm.getName());
        redirectAttributes.addFlashAttribute("email", signUpForm.getEmail());
        return "redirect:/account";
    }
}
