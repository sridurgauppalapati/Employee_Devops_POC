package com.employee.tests.pages;

import com.employee.tests.config.TestConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {

    private final WebDriver driver;

    private static final By HOME_PAGE = By.cssSelector("[data-testid='home-page']");
    private static final By HOME_TITLE = By.cssSelector("[data-testid='home-title']");
    private static final By SEARCH_LINK = By.cssSelector("[data-testid='search-employee-link']");
    private static final By APP_HEADER = By.cssSelector("[data-testid='app-header']");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public HomePage open() {
        driver.get(TestConfig.baseUrl() + "/home");
        return this;
    }

    public boolean isLoaded() {
        return driver.findElement(HOME_PAGE).isDisplayed()
                && driver.findElement(HOME_TITLE).isDisplayed()
                && driver.findElement(APP_HEADER).isDisplayed();
    }

    public String getTitleText() {
        return driver.findElement(HOME_TITLE).getText();
    }

    public SearchEmployeePage goToSearch() {
        driver.findElement(SEARCH_LINK).click();
        return new SearchEmployeePage(driver);
    }
}
