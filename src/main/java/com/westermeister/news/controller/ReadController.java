package com.westermeister.news.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.westermeister.news.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Handles all GET requests.
 */
@Controller
public class ReadController extends BaseController {
    /**
     * Inject dependencies.
     *
     * @param controllerHelper  contains helpful utilities for controller logic
     */
    public ReadController(UserRepository userRepo) {
        super(userRepo);
    }

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
        populateSignUpModel(model);
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
     * @param principal  currently signed-in user
     * @param redirect   used to send error messages to user if necessary
     * @param request    used to
     * @param model      will be populated with required data
     * @return           name of the thymeleaf template to render
     */
    @GetMapping("/account")
    public String account(
        Principal principal,
        RedirectAttributes redirect,
        HttpServletRequest request,
        Model model
    ) {
        String error = populateAccountModel(model, principal, request, redirect);
        if (!error.isEmpty()) return error;
        return "account";
    }

    /**
     * Process request for fake success page for spam bots.
     *
     * @return  name of the relevant template
     */
    @GetMapping("/success")
    public String fakeSuccess() {
        return "fake-success";
    }
}
