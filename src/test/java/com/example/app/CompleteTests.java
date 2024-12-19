package com.example.app;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CompleteTests extends BaseTest{

    private final String email = "juan@gmail.com";

    private void navigateToCompletePage() {
        page.fill("#emailAddress", email);
        page.fill("#password", "password123");
        page.fill("#confirmPassword", "password123");
        page.click(".wizard-button.primary");

        page.fill("#firstName", "Juan");
        page.fill("#lastName", "Mangubat");
        page.fill("#addressLine1", "123 Sesame Street");
        page.fill("#postcode", "1234");
        page.fill("#city", "Transylvania");
        page.selectOption("#state", "1: ACT");
        page.click(".wizard-button.primary");

        page.fill("#cardHolderName", "Juan Mangubat");
        page.locator("#cardTypeVISA").click();
        page.fill("#cardNumber", "1234123412341234");
        page.fill("#cardCVV", "123");
        page.selectOption("#cardExpiryMonth", "1: 1");
        page.fill("#cardExpiryYear", "2040");
        page.click(".wizard-button.primary");

        Locator textArea = page.locator("#termsAndConditions");
        textArea.evaluate("element => element.scrollTop = element.scrollHeight");
        textArea.evaluate("element => element.scrollTop = element.scrollHeight");
        Locator checkbox = page.locator("#agreedToTerms");
        checkbox.click();
        page.click(".wizard-button.primary");
    }

    @Test
    public void testCompletePageDoesNotDisplayUserName() {
        navigateToCompletePage();
        Locator pElem = page.locator("app-register-complete-page p").nth(0);

        //displays welcome without a name
        assertThat(pElem).hasText("Welcome, .");
    }

    @Test
    public void testCompletePageDisplaysSuccessMessageWithEmail() {
        navigateToCompletePage();
        Locator pElem = page.locator("app-register-complete-page p").nth(1);

        //displays success message with user's email
        assertThat(pElem).containsText("Your user account " + email + " has been successfully registered.");
    }
}
