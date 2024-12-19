package com.example.app;

import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentTests extends BaseTest {
    private void navigateToPaymentPage() {
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
    }

//    @Test
//    public void testEnterAcceptableEmailAndPasswords(){
//        navigateToPaymentPage();
//        Locator paymentPage = page.locator(".wizard-content app-register-payment-page");
//        assertThat(paymentPage).isVisible();
//    }

    @Test
    public void testCardHolderNameAcceptsAnyValue(){
        //enter mixed character value to card holder name field
        navigateToPaymentPage();
        page.fill("#cardHolderName", "123!@#abc");

        //no error message
        Locator validationMessage = page.locator(".validation-messages").nth(0);
        assertThat(validationMessage.locator("*")).hasCount(0);
    }

    @Test
    public void testCardNumberDoesNotRestrictMaxLength(){
        //enter large number to card number
        navigateToPaymentPage();
        page.fill("#cardNumber", "111111111111111");

        //no error message
        Locator validationMessage = page.locator(".validation-messages").nth(2);
        assertThat(validationMessage.locator("*")).hasCount(0);
    }

    @Test
    public void testCardNumberDoesNotRestrictMinLength(){
        //enter small number to card number
        navigateToPaymentPage();
        page.fill("#cardNumber", "1");

        //no error message
        Locator validationMessage = page.locator(".validation-messages").nth(2);
        assertThat(validationMessage.locator("*")).hasCount(0);
    }

    @Test
    public void testCardNumberAllowsNegativeNumber(){
        //enter negative number to card number
        navigateToPaymentPage();
        page.fill("#cardNumber", "-10");

        //no error message
        Locator validationMessage = page.locator(".validation-messages").nth(2);
        assertThat(validationMessage.locator("*")).hasCount(0);
    }

    @Test
    public void testCardCVVDoesNotRestrictMaxLength(){
        //enter large number to card CVV
        navigateToPaymentPage();
        page.fill("#cardCVV", "11111111111");

        //no error message
        Locator validationMessage = page.locator(".validation-messages").nth(3);
        assertThat(validationMessage.locator("*")).hasCount(0);
    }

    @Test
    public void testCardCVVDoesNotRestrictMinLength(){
        //enter small number to card CVV
        navigateToPaymentPage();
        page.fill("#cardCVV", "1");

        //no error message
        Locator validationMessage = page.locator(".validation-messages").nth(3);
        assertThat(validationMessage.locator("*")).hasCount(0);
    }


    @Test
    public void testCardCVVAllowsNegativeNumber(){
        //enter negative number for card CVV
        navigateToPaymentPage();
        page.fill("#cardCVV", "-10");

        //no error message
        Locator validationMessage = page.locator(".validation-messages").nth(3);
        assertThat(validationMessage.locator("*")).hasCount(0);
    }

    @Test
    public void testCardExpiryMonthDropdownDisplaysSelectCardType(){
        //open up card expiry month dropdown to view "Select a card type"
        navigateToPaymentPage();
        String dropDownVal = page.locator("//*[@id=\"cardExpiryMonth\"]/option[1]").innerText();

        //first option equals "Select a card type"
        assertEquals("-- Select a card type --", dropDownVal);
    }

    @Test
    public void testCardExpiryYearDoesNotRestrictMaxLength(){
        //enter large number to card expiry year
        navigateToPaymentPage();
        page.fill("#cardExpiryYear", "11111111111");

        //no error message
        Locator validationMessage = page.locator(".validation-messages").nth(5);
        assertThat(validationMessage.locator("*")).hasCount(0);
    }

    @Test
    public void testCardExpiryYearDoesNotRestrictMinLength(){
        //enter small number to card expiry year
        navigateToPaymentPage();
        page.fill("#cardExpiryYear", "1");

        //no error message
        Locator validationMessage = page.locator(".validation-messages").nth(5);
        assertThat(validationMessage.locator("*")).hasCount(0);
    }


    @Test
    public void testCardExpiryYearAllowsNegativeNumber(){
        //enter negative number for card expiry year
        navigateToPaymentPage();
        page.fill("#cardExpiryYear", "-10");

        //no error message
        Locator validationMessage = page.locator(".validation-messages").nth(5);
        assertThat(validationMessage.locator("*")).hasCount(0);
    }

    @Test
    public void testAcceptExpiredMonth(){
        //select a past month
        navigateToPaymentPage();
        page.fill("#cardHolderName", "Juan Mangubat");
        page.locator("#cardTypeVISA").click();
        page.fill("#cardNumber", "1234123412341234");
        page.fill("#cardCVV", "123");
        page.selectOption("#cardExpiryMonth", "1: 1");
        page.fill("#cardExpiryYear", "2024");
        page.click(".wizard-button.primary");

        //proceed to terms page
        Locator termsPage = page.locator(".wizard-content app-register-terms-page");
        assertThat(termsPage).isVisible();
    }

    @Test
    public void testAcceptExpiredYear(){
        //select a past year
        navigateToPaymentPage();
        page.fill("#cardHolderName", "Juan Mangubat");
        page.locator("#cardTypeVISA").click();
        page.fill("#cardNumber", "1234123412341234");
        page.fill("#cardCVV", "123");
        page.selectOption("#cardExpiryMonth", "1: 1");
        page.fill("#cardExpiryYear", "2000");
        page.click(".wizard-button.primary");

        //proceed to terms page
        Locator termsPage = page.locator(".wizard-content app-register-terms-page");
        assertThat(termsPage).isVisible();
    }

    @Test
    public void testRequiredInputValidationMessages(){
        //press next before entering required values
        navigateToPaymentPage();
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
        //enter values for all required inputs/selectors/radios
        navigateToPaymentPage();
        page.fill("#cardHolderName", "Juan Mangubat");
        page.locator("#cardTypeVISA").click();
        page.fill("#cardNumber", "1234123412341234");
        page.fill("#cardCVV", "123");
        page.selectOption("#cardExpiryMonth", "1: 1");
        page.fill("#cardExpiryYear", "2040");
        page.click(".wizard-button.primary");

        //proceed to terms page
        Locator termsPage = page.locator(".wizard-content app-register-terms-page");
        assertThat(termsPage).isVisible();
    }

    @Test
    public void testContactBackButton(){
        //click the back button
        navigateToPaymentPage();
        Locator backButton = page.locator(".wizard-button").nth(0);
        backButton.click();

        //returns to contact page
        Locator contactPage = page.locator(".wizard-content app-register-contact-page");
        assertThat(contactPage).isVisible();
    }
}
