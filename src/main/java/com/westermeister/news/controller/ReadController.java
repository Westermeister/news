package com.westermeister.news.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.westermeister.news.entity.User;
import com.westermeister.news.form.SignUpForm;
import com.westermeister.news.form.UpdateEmailForm;
import com.westermeister.news.form.UpdateNameForm;
import com.westermeister.news.form.UpdatePasswordForm;
import com.westermeister.news.util.ControllerHelper;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Handles all GET requests.
 */
@Controller
public class ReadController {
    private ControllerHelper controllerHelper;

    /**
     * Inject dependencies.
     *
     * @param controllerHelper  contains helpful utilities for controller logic
     */
    public ReadController(ControllerHelper controllerHelper) {
        this.controllerHelper = controllerHelper;
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
     * @param principal           currently signed-in user
     * @param redirectAttributes  used to send error messages to user if necessary
     * @param model               context for rendering
     * @return                    name of the thymeleaf template to render
     */
    @GetMapping("/account")
    public String account(
        Principal principal,
        RedirectAttributes redirectAttributes,
        HttpServletRequest httpServletRequest,
        Model model
    ) {
        User user = controllerHelper.loadUserFromPrincipal(principal);
        if (user == null) {
            return controllerHelper.handleMissingUser(httpServletRequest, redirectAttributes, principal);
        }

        model.addAttribute("currentName", user.getName());
        model.addAttribute("currentEmail", user.getEmail());

        if (!model.containsAttribute("updateNameForm")) {
            model.addAttribute("updateNameForm", new UpdateNameForm());
        }
        if (!model.containsAttribute("updateEmailForm")) {
            model.addAttribute("updateEmailForm", new UpdateEmailForm());
        }
        if (!model.containsAttribute("updatePasswordForm")) {
            model.addAttribute("updatePasswordForm", new UpdatePasswordForm());
        }
        return "account";
    }
}
