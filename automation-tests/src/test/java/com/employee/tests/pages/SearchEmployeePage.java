package com.employee.tests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SearchEmployeePage {

    private final WebDriver driver;

    private static final By SEARCH_TITLE = By.cssSelector("[data-testid='search-title']");
    private static final By SEARCH_INPUT = By.cssSelector("[data-testid='search-input']");
    private static final By SEARCH_BUTTON = By.cssSelector("[data-testid='search-button']");
    private static final By RESULT_ROWS = By.cssSelector("[data-testid^='search-result-']");
    private static final By NO_RESULTS = By.cssSelector("[data-testid='no-search-results']");

    public SearchEmployeePage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isLoaded() {
        return driver.findElement(SEARCH_TITLE).isDisplayed();
    }

    public SearchEmployeePage searchFor(String query) {
        driver.findElement(SEARCH_INPUT).clear();
        driver.findElement(SEARCH_INPUT).sendKeys(query);
        driver.findElement(SEARCH_BUTTON).click();
        return this;
    }

    public int getResultCount() {
        return driver.findElements(RESULT_ROWS).size();
    }

    public boolean hasNoResultsMessage() {
        return !driver.findElements(NO_RESULTS).isEmpty();
    }

    public boolean resultsContainText(String expectedText) {
        List<WebElement> rows = driver.findElements(RESULT_ROWS);
        return rows.stream().anyMatch(row -> row.getText().toLowerCase().contains(expectedText.toLowerCase()));
    }
}
