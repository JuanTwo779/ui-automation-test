package com.example.app;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.assertions.LocatorAssertions.*;

public class RegistrationTests extends BaseTest {

    @Test
    public void testNextButtonProgressedAndActiveClass() {
        //TA. next page item is 'active' when next is clicked without entering required/valid input
        page.click(".wizard-button.primary");

        //assert the next page item is 'active' and current page item is 'progressed'
        Locator contactPageItem = page.locator("app-wizard-page-item[title='Contact'] >> .wizard-page-item");
        Locator regPageItem = page.locator("app-wizard-page-item[title='Registration'] >> .wizard-page-item");
        assertThat(contactPageItem).hasClass("wizard-page-item active");
        assertThat(regPageItem).hasClass("wizard-page-item progressed");
    }

    //responsiveness test

    @Test
    public void testInvalidEmail() {
        //T1. error message appears when entering invalid email
        page.fill("#emailAddress", "mail");
        page.click(".wizard-button.primary");
        Locator error = page.locator(".validation.error span");
        assertThat(error).hasText("Invalid email address.");
    }

    @Test
    public void testNumericalUsernameEmail() {
        //T2. error message appears when entering email with numbers in username
        page.fill("#emailAddress", "Juan123@gmail.com");
        page.click(".wizard-button.primary");
        Locator error = page.locator(".validation.error span");
        assertThat(error).hasText("Invalid email address.");
    }

    @Test
    public void testInvalidEmailDomain(){
        //T3. system validates incorrect domain email
        page.fill("#emailAddress", "Juan@gmail");
        page.click(".wizard-button.primary");

        //no client-side validation
        Locator validationMessage = page.locator("div.validation-messages").nth(0);
        assertThat(validationMessage).hasText("");
        assertThat(validationMessage.locator("*")).hasCount(0);

        //no server-side validation
        page.fill("#emailAddress", "Juan@gmail");
        page.fill("#password", "password123");
        page.fill("#confirmPassword", "password123");
        page.click(".wizard-button.primary");
        Locator contactPage = page.locator(".wizard-content app-register-contact-page");
        assertThat(contactPage).isVisible();
    }

    @Test
    public void testWeakPassword() {
        //T4. success toast appears and the user proceeds to Contact page when weak password is entered
        page.fill("#emailAddress", "Juan@gmail.com");
        page.fill("#password", "123");
        page.fill("#confirmPassword", "123");


        //no client-side validations
        Locator validationMessage = page.locator("div.validation-messages").nth(1);
        assertThat(validationMessage).hasText("");
        assertThat(validationMessage.locator("*")).hasCount(0);

        //no server-side validations
        page.click(".wizard-button.primary");
        Locator contactPage = page.locator(".wizard-content app-register-contact-page");
        assertThat(contactPage).isVisible();
    }

    @Test
    public void testEmptyEmail(){
        //T5: error message displayed when email field left blank
        page.fill("#emailAddress", "");
        page.fill("#password", "password123");
        page.fill("#confirmPassword", "password123");
        page.click(".wizard-button.primary");

        //validation messages
        Locator validationMessage = page.locator("div.validation-messages").nth(0);
        assertThat(validationMessage.locator("*")).hasCount(2);
        assertThat(validationMessage).hasText("Email Address is required");
    }

    @Test
    public void testEmptyPassword(){
        //T6: error message displayed when password field left blank
        page.fill("#emailAddress", "juan@gmail.com");
        page.fill("#password", "");
        page.fill("#confirmPassword", "password123");
        page.click(".wizard-button.primary");

        //validation messages
        Locator validationMessage = page.locator("div.validation-messages").nth(1);
        assertThat(validationMessage.locator("*")).hasCount(2);
        assertThat(validationMessage).hasText("Password is required.");
        assertThat(page.locator("//span[text()='Password is required.']")).isVisible();
    }

    @Test
    public void testEmptyConfirmPassword(){
        //T7: error message displayed when confirm password field left blank
        page.fill("#emailAddress", "juan@gmail.com");
        page.fill("#password", "password123");
        page.fill("#confirmPassword", "");
        page.click(".wizard-button.primary");

        //validation messages
        Locator validationMessage = page.locator("div.validation-messages").nth(2);
        assertThat(validationMessage.locator("*")).hasCount(2);
        assertThat(validationMessage).hasText("Confirm Password is required");
        assertThat(page.locator("//span[text()='Confirm Password is required']")).isVisible();
    }

    @Test
    public void testPasswordsNotMatch(){
        //T8&9: error message displayed when confirm password field left blank
        page.fill("#emailAddress", "juan@gmail.com");
        page.fill("#password", "password123");
        page.fill("#confirmPassword", "username321");
        page.click(".wizard-button.primary");

        //validation messages
        Locator validationMessage = page.locator("div.validation-messages").nth(2);
        assertThat(validationMessage.locator("*")).hasCount(2);
        //typo in error message
        assertThat(validationMessage).hasText("Passwords do not mach");
        assertThat(page.locator("//span[text()='Passwords do not mach']")).isVisible();
    }

    @Test
    public void testUseExistingEmail(){
        //T10. error toast when existing email is used to register
        page.fill("#emailAddress", "adam@orikan.com");
        page.fill("#password", "password123");
        page.fill("#confirmPassword", "password123");
        page.click(".wizard-button.primary");

        //error toast message
        Locator toast = page.locator("app-toast-overlay .toast-message.error.active");
        assertThat(toast).isVisible();
        assertThat(toast).hasText("Email address is already registered");
    }

    @Test
    public void testEnterAcceptableEmailAndPasswords(){
        //T10. error toast when existing email is used to register
        page.fill("#emailAddress", "juan@gmail.com");
        page.fill("#password", "password123");
        page.fill("#confirmPassword", "password123");
        page.click(".wizard-button.primary");

        //error toast message
        Locator toast = page.locator("app-toast-overlay .toast-message.success");
        assertThat(toast).isVisible();
        assertThat(toast).hasText("Email address is available for registration");

        //progress to next page
        page.click(".wizard-button.primary");
        Locator contactPage = page.locator(".wizard-content app-register-contact-page");
        assertThat(contactPage).isVisible();
    }

}
