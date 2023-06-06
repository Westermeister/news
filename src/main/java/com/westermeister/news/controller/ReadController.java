package com.westermeister.news.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.westermeister.news.entity.User;
import com.westermeister.news.form.SignUpForm;
import com.westermeister.news.form.UpdateNameForm;
import com.westermeister.news.repository.UserRepository;

/**
 * Handles all GET requests.
 */
@Controller
public class ReadController {
    private UserRepository userRepo;

    /**
     * Inject dependencies.
     *
     * @param userRepo  used for reading user data
     */
    public ReadController(UserRepository userRepo) {
        this.userRepo = userRepo;
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
        Model model
    ) {
        List<User> users = userRepo.findFirstByEmail(principal.getName());
        if (users.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                "headerErrorMessage",
                "We couldn't find your account. It may have been deleted by accident."
            );
            return "redirect:/signup";
        }
        User user = users.get(0);

        if (!model.containsAttribute("updateNameForm")) {
            UpdateNameForm updateNameForm = new UpdateNameForm();
            updateNameForm.setName(user.getName());
            model.addAttribute("updateNameForm", updateNameForm);
        }
        return "account";
    }
}
