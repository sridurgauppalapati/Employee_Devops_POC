package com.employee.tests.pages;

import com.employee.tests.config.TestConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private final WebDriver driver;

    private static final By USERNAME = By.cssSelector("[data-testid='username-input']");
    private static final By PASSWORD = By.cssSelector("[data-testid='password-input']");
    private static final By LOGIN_BUTTON = By.cssSelector("[data-testid='login-button']");
    private static final By LOGIN_TITLE = By.cssSelector("[data-testid='login-title']");
    private static final By LOGIN_ERROR = By.cssSelector("[data-testid='login-error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public LoginPage open() {
        driver.get(TestConfig.baseUrl() + "/login");
        return this;
    }

    public LoginPage login(String username, String password) {
        driver.findElement(USERNAME).clear();
        driver.findElement(USERNAME).sendKeys(username);
        driver.findElement(PASSWORD).clear();
        driver.findElement(PASSWORD).sendKeys(password);
        driver.findElement(LOGIN_BUTTON).click();
        return this;
    }

    public boolean isDisplayed() {
        return driver.findElement(LOGIN_TITLE).isDisplayed();
    }

    public boolean isErrorDisplayed() {
        return !driver.findElements(LOGIN_ERROR).isEmpty();
    }
}
