package com.westermeister.news.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.westermeister.news.form.SignUpForm;

/**
 * Handles all GET requests.
 */
@Controller
public class ReadController {
    /**
     * Process request for homepage.
     *
     * @param model  context for rendering
     * @return       name of the thymeleaf template to render
     */
    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    /**
     * Process request for sign-up page.
     *
     * @param model  context for rendering
     * @return       name of the thymeleaf template to render
     */
    @GetMapping("/signup")
    public String signUp(Model model) {
        if (!model.containsAttribute("signUpForm")) {
            model.addAttribute("signUpForm", new SignUpForm());
        }
        return "signup";
    }

    /**
     * Process request for sign-in page.
     *
     * @param model  context for rendering
     * @return       name of the thymeleaf template to render
     */
    @GetMapping("/signin")
    public String signIn(Model model) {
        return "signin";
    }

    /**
     * Process request for account page.
     *
     * @param model  context for rendering
     * @return       name of the thymeleaf template to render
     */
    @GetMapping("/account")
    public String account(Model model) {
        return "account";
    }
}
