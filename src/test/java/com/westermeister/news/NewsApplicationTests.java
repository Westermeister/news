package com.westermeister.news;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;

@SpringBootTest
class NewsApplicationTests {
    private String site = "http://localhost:8080";
    private Playwright playwright;
    private Browser browser;
    private Page page;

    // This method creates a simple test user.
    // Must navigate to sign-up page before calling this.
    private void fillSignUpFormAndSubmit(Page page) {
        page.getByLabel("Full name").fill("Mario Mario");
        page.getByLabel("Email address").fill("mario.mario@mushroom.org");
        page.getByLabel("Password", new Page.GetByLabelOptions().setExact(true)).fill("password");
        page.getByLabel("Type password again").fill("password");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign up")).click();
        page.waitForURL("**/account");
    }

    // Must navigate to account page before calling this.
    private void deleteAccount(Page page) {
        // We have two delete-account buttons.
        // The first opens the confirmation modal, the second actually does the deletion.
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Delete account")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Delete account")).all().get(1).click();
    }

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        page = browser.newPage();
    }

    @AfterEach
    void tearDown() {
        playwright.close();
    }

    // Basic sanity check.
    @Test
    void testHomePage() {
        page.navigate(site);
        assertEquals(page.title(), "News Summary");
    }

    // Test user flow for basic account operations: signing up, modifying account details, etc.
    @Test
    void testCreateUpdateAndDeleteAccount() {
        // Test signing up.
        page.navigate(site + "/signup");
        assertEquals(page.title(), "Sign up - News Summary");
        assertTrue(page.getByRole(
            AriaRole.HEADING,
            new Page.GetByRoleOptions().setName("Sign up").setExact(true)
        ).isVisible());
        fillSignUpFormAndSubmit(page);

        // Basic sanity check that we're on the account page.
        assertEquals(page.title(), "My account - News Summary");
        assertTrue(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("My account")).isVisible());

        // Try changing the user's name.
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Change name")).click();
        page.getByLabel("New name").fill("Luigi Mario");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Change name")).all().get(1).click();
        page.waitForURL("**/account");
        assertTrue(page.getByText("Luigi Mario").isVisible());

        // Try changing the user's email.
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Change email")).click();
        page.getByLabel("New email").fill("luigi.mario@mushroom.org");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Change email")).all().get(1).click();
        page.waitForURL("**/account");
        assertTrue(page.getByText("luigi.mario@mushroom.org").isVisible());

        // Try changing the user's password.
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Change password")).click();
        page.getByLabel("Current password").fill("password");
        page.getByLabel("New password", new Page.GetByLabelOptions().setExact(true)).fill("newpassword");
        page.getByLabel("Type new password").fill("newpassword");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Change password")).all().get(1).click();
        page.waitForURL("**/account");

        // Sign out and try signing back in with the new password.
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign out")).click();
        page.navigate(site + "/signin");
        page.getByLabel("Email address").fill("luigi.mario@mushroom.org");
        page.getByLabel("Password").fill("newpassword");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in")).click();
        page.waitForURL("**/");
        page.navigate(site + "/account");
        assertEquals(page.title(), "My account - News Summary");

        // Try deleting account.
        deleteAccount(page);
        page.waitForURL("**/");
        assertEquals(page.title(), "News Summary");
    }

    // Basic sanity check that form validation is working in sign-up page.
    @Test
    void testSignUpFormValidation() {
        page.navigate(site + "/signup");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign up")).click();
        page.waitForURL("**/api/create/user");
        assertTrue(page.getByText("Enter your full name.").isVisible());
    }

    // Basic sanity check that form validation is working in sign-in page.
    @Test
    void testSignInFormValidation() {
        page.navigate(site + "/signin");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in")).click();
        page.waitForURL("**/signin?error");
        assertTrue(page.getByText("Incorrect email address or password.").isVisible());
    }
}
