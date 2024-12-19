package com.example.app;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext browserContext;
    protected Page page;

    @BeforeEach
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        browserContext = browser.newContext();
        page = browserContext.newPage();
        page.navigate("https://orikan-ui-automation-test.azurewebsites.net/");
    }

    @AfterEach
    public void tearDown() {
//        page.close();
        browser.close();
        playwright.close();

    }
}
