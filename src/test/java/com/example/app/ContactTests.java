package com.example.app;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ContactTests extends BaseTest{

    private void navigateToContactPage() {
        page.fill("#emailAddress", "juan@gmail.com");
        page.fill("#password", "password123");
        page.fill("#confirmPassword", "password123");
        page.click(".wizard-button.primary");
    }

//    @Test
//    public void testContactPage(){
//        navigateToContactPage();
//        Locator contactPage = page.locator(".wizard-content app-register-contact-page");
//        assertThat(contactPage).isVisible();
//    }

    @Test
    public void testNonAlphaCharInFirstName(){
        //T12. non-alpha characters in first name field
        navigateToContactPage();
        page.fill("#firstName", "123%^&");

        //no error messages
        Locator validationMessage = page.locator(".validation-messages").nth(0);
        assertThat(validationMessage.locator("*")).hasCount(0);
    }

    @Test
    public void testNonAlphaCharInLastName(){
        //T13. non-alpha characters in first name field
        navigateToContactPage();
        page.fill("#lastName", "123%^&");

        //error messages because last name validation message linked to first name field
        Locator validationMessage = page.locator(".validation-messages").nth(1);
        assertThat(validationMessage.locator("*")).hasCount(2);

        //server-side validation checks
        page.fill("#firstName", "Juan");
        page.fill("#addressLine1", "123 Sesame Street");
        page.fill("#postcode", "1234");
        page.fill("#city", "Transylvania");
        page.selectOption("#state", "1: ACT");
        page.click(".wizard-button.primary");

        Locator paymentPage = page.locator(".wizard-content app-register-payment-page");
        assertThat(paymentPage).isVisible();
    }

    @Test
    public void testLastNameErrorMessageTiedToFirstName(){
        //T14. an invalid value in the first name field displays an error message under the last name field
        navigateToContactPage();
        page.fill("#firstName", "");

        //no error messages
        Locator validationMessage = page.locator(".validation-messages").nth(1);
        assertThat(validationMessage).containsText("required");
    }

    @Test
    public void testPreferredFullNameContainExtraSpace(){
        //T15. enter first name and last name, leave middle name blank, preferred name has extra space
        navigateToContactPage();
        page.fill("#firstName", "Juan");
        page.fill("#lastName", "Mangubat");

        //extra space in preferred name input
        String preferred = page.locator("#preferredFullName").inputValue();
        assertEquals("Juan  Mangubat", preferred);
    }

    @Test
    public void testNoValidationForAddressField(){
        //T16. any characters allowed for address line 1 input
        navigateToContactPage();
        page.fill("#addressLine1", "The Moon !@#$");

        //no error messages
        Locator validationMessage = page.locator(".validation-messages").nth(2);
        assertThat(validationMessage.locator("*")).hasCount(0);
    }

    @Test
    public void testDuplicateAddressFields(){
        //T17. duplicate addresses allowed
        navigateToContactPage();
        page.fill("#addressLine1", "207–361 Flinders Street");
        page.fill("#addressLine2", "207–361 Flinders Street");
        page.fill("#addressLine3", "207–361 Flinders Street");

        //no error messages
        Locator validationMessage = page.locator(".validation-messages").nth(2);
        assertThat(validationMessage.locator("*")).hasCount(0);
    }

    @Test
    public void testNegativeNumberForPostcodeField(){
        //negative number for postcode
        navigateToContactPage();
        page.fill("#postcode", "-1");

        //no error messages
        Locator validationMessage = page.locator(".validation-messages").nth(3);
        assertThat(validationMessage.locator("*")).hasCount(0);
    }

    @Test
    public void testLetterForPostcodeField(){
        //letters for postcode
        navigateToContactPage();
        page.fill("#postcode", "letter");

        //no error messages
        Locator validationMessage = page.locator(".validation-messages").nth(3);
        assertThat(validationMessage.locator("*")).hasCount(0);
    }

    @Test
    public void testInputLengthForPostcodeField(){
        //letters for postcode
        navigateToContactPage();
        page.fill("#postcode", "1234567890abcdefghij");

        //no error messages
        Locator validationMessage = page.locator(".validation-messages").nth(3);
        assertThat(validationMessage.locator("*")).hasCount(0);
    }

    @Test
    public void testNoValidationForCityField(){
        //T19. any characters and input length allowed for city field
        navigateToContactPage();
        page.fill("#city", "-1 City ..................");

        //no error messages
        Locator validationMessage = page.locator(".validation-messages").nth(4);
        assertThat(validationMessage.locator("*")).hasCount(0);
    }

    @Test
    public void testDuplicateInStateDropdown(){
        //T20. state selection field contains duplicate values
        navigateToContactPage();
        String val1 = page.locator("//*[@id=\"state\"]/option[6]").innerText();
        String val2 = page.locator("//*[@id=\"state\"]/option[7]").innerText();

        //two options possess the same text
        assertEquals(val1, val2);
    }

    @Test
    public void testRequiredInputValidationMessages(){
        //21. each required input displays error message when not filled in
        navigateToContactPage();
        page.fill("#firstName", "");
        page.fill("#lastName", "");
        page.fill("#addressLine1", "");
        page.fill("#postcode", "");
        page.fill("#city", "");
        page.click(".wizard-button.primary");

        //error message contains "required"
        Locator vm1 = page.locator(".validation-messages").nth(0);
        assertThat(vm1).containsText("required");
        Locator vm2 = page.locator(".validation-messages").nth(1);
        assertThat(vm2).containsText("required");
        Locator vm3 = page.locator(".validation-messages").nth(2);
        assertThat(vm3).containsText("required");
        Locator vm4 = page.locator(".validation-messages").nth(3);
        assertThat(vm4).containsText("required");
        Locator vm5 = page.locator(".validation-messages").nth(4);
        assertThat(vm5).containsText("required");
        Locator vm6 = page.locator(".validation-messages").nth(5);
        assertThat(vm6).containsText("required");
    }

    @Test
    public void testCompleteRequiredInputs(){
        //22. proceed to next page after entering required inputs
        navigateToContactPage();
        page.fill("#firstName", "Juan");
        page.fill("#lastName", "Mangubat");
        page.fill("#addressLine1", "123 Sesame Street");
        page.fill("#postcode", "1234");
        page.fill("#city", "Transylvania");
        page.selectOption("#state", "1: ACT");
        page.click(".wizard-button.primary");

        //display payment page
        Locator paymentPage = page.locator(".wizard-content app-register-payment-page");
        assertThat(paymentPage).isVisible();
    }

    @Test
    public void testCompleteAllInputs(){
        //23. proceed to next page after entering all inputs
        navigateToContactPage();
        page.fill("#firstName", "Juan");
        page.fill("#middleName", "Messi");
        page.fill("#lastName", "Mangubat");
        page.fill("#preferredFullName", "juanmangubat");
        page.fill("#addressLine1", "123 Sesame Street");
        page.fill("#addressLine2", "456 Sesame Street");
        page.fill("#addressLine3", "789 Sesame Street");
        page.fill("#postcode", "1234");
        page.fill("#city", "Transylvania");
        page.selectOption("#state", "1: ACT");
        page.click(".wizard-button.primary");

        //display payment page
        Locator paymentPage = page.locator(".wizard-content app-register-payment-page");
        assertThat(paymentPage).isVisible();
    }

    @Test
    public void testContactBackButton(){
        //T24. click the back button
        navigateToContactPage();
        Locator backButton = page.locator(".wizard-button").nth(0);
        backButton.click();

        //returns to registration/user page
        Locator registrationPage = page.locator(".wizard-content app-register-user-page");
        assertThat(registrationPage).isVisible();
    }


}
