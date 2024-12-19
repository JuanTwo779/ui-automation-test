package com.example.app;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class TermsConditionsTests extends BaseTest{
    private void navigateToTermsPage() {
        page.fill("#emailAddress", "juan@gmail.com");
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
    }

//    @Test
//    public void testTermsPage(){
//        navigateToTermsPage();
//        Locator termsPage = page.locator(".wizard-content app-register-terms-page");
//        assertThat(termsPage).isVisible();
//    }

    @Test
    public void testImmediateSubmit(){
        //click submit button
        navigateToTermsPage();
        page.click(".wizard-button.primary");

        //error message displayed
        Locator validationMessage = page.locator(".validation-messages").nth(0);
        assertThat(validationMessage).hasText("You must first read all the terms and conditions before submitting");
    }

    @Test
    public void testTermsCheckboxEnablesAfterScroll(){
        navigateToTermsPage();

        //assert the checkbox is disabled initially
        Locator checkbox = page.locator("#agreedToTerms");
        assertTrue(checkbox.isDisabled());

        //scroll the terms and conditions textarea to the bottom
        Locator textArea = page.locator("#termsAndConditions");
        textArea.evaluate("element => element.scrollTop = element.scrollHeight");

        //assert the checkbox is enabled after scrolling
        checkbox = page.locator("#agreedToTerms");
        checkbox.waitFor();
        assertFalse(checkbox.isDisabled());
    }

    @Test
    public void testSubmitBeforeClickingTermsCheckbox(){
        //scroll the terms and conditions textarea to the bottom and click submit
        navigateToTermsPage();
        Locator textArea = page.locator("#termsAndConditions");
        textArea.evaluate("element => element.scrollTop = element.scrollHeight");
        page.click(".wizard-button.primary");

        //error message displayed
        Locator validationMessage = page.locator(".validation-messages").nth(1);
        assertThat(validationMessage).hasText("You must agree to these terms and conditions before submitting");
    }

    @Test
    public void testCompleteAllRequirementsAndSubmitSuccessToast(){
        //scroll the terms, click checkbox, submit
        navigateToTermsPage();
        Locator textArea = page.locator("#termsAndConditions");
        textArea.evaluate("element => element.scrollTop = element.scrollHeight");
        Locator checkbox = page.locator("#agreedToTerms");
        checkbox.click();
        page.click(".wizard-button.primary");

        //proceed to complete page
        Locator completePage = page.locator(".wizard-content app-register-complete-page");
        assertThat(completePage).isVisible();

        Locator toast = page.locator("app-toast-overlay .toast-message.success");
        assertThat(toast).isVisible();
        assertThat(toast).containsText("Successfully registered user");
    }

    @Test
    public void testTermsBackButton(){
        //click back button
        navigateToTermsPage();
        Locator backButton = page.locator(".wizard-button").nth(0);
        backButton.click();

        //returns to payment page
        Locator paymentPage = page.locator(".wizard-content app-register-payment-page");
        assertThat(paymentPage).isVisible();
    }

}
