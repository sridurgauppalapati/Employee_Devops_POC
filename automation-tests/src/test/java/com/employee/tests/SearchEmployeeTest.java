package com.employee.tests;

import com.employee.tests.base.BaseTest;
import com.employee.tests.config.TestConfig;
import com.employee.tests.pages.HomePage;
import com.employee.tests.pages.LoginPage;
import com.employee.tests.pages.SearchEmployeePage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("Employee Management")
@Feature("Search Employee")
public class SearchEmployeeTest extends BaseTest {

    @BeforeMethod
    public void login() {
        new LoginPage(driver).open().login(TestConfig.username(), TestConfig.password());
    }

    @Test(description = "Verify search returns matching employee")
    @Description("Smoke test: search by first name returns seeded employee Jane Doe")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldFindEmployeeByFirstName() {
        SearchEmployeePage searchPage = new HomePage(driver).open().goToSearch();
        Assert.assertTrue(searchPage.isLoaded(), "Search page should load");

        searchPage.searchFor("Jane");
        Assert.assertTrue(searchPage.getResultCount() >= 1, "At least one search result expected");
        Assert.assertTrue(searchPage.resultsContainText("Jane"), "Results should contain Jane");
    }

    @Test(description = "Verify search with no match shows empty message")
    @Description("Smoke test: unknown search term shows no results message")
    @Severity(SeverityLevel.NORMAL)
    public void shouldShowNoResultsForUnknownQuery() {
        SearchEmployeePage searchPage = new HomePage(driver).open().goToSearch();
        searchPage.searchFor("zzzz-not-found-employee");
        Assert.assertTrue(searchPage.hasNoResultsMessage(), "No results message should be displayed");
    }
}
