package com.westermeister.news.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.westermeister.news.repository.SnippetRepository;
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
     * @param userRepo     used to initialize base class
     * @param snippetRepo  used to initialize base class
     */
    public ReadController(UserRepository userRepo, SnippetRepository snippetRepo) {
        super(userRepo, snippetRepo);
    }

    /**
     * Process request for homepage.
     *
     * @param model  context for rendering
     * @return       name of the thymeleaf template to render
     */
    @GetMapping("/")
    public String index(Model model) {
        populateIndexModel(model);
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
        if (!error.isEmpty()) {
            return error;
        }
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

    /**
     * Process request for terms of service.
     *
     * @return  name of the relevant template
     */
    @GetMapping("/terms-of-service")
    public String termsOfService() {
        return "terms-of-service";
    }

    /**
     * Process request for privacy policy.
     *
     * @return  name of the relevant template
     */
    @GetMapping("/privacy-policy")
    public String privacyPolicy() {
        return "privacy-policy";
    }
}
