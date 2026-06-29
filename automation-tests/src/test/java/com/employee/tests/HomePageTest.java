package com.employee.tests;

import com.employee.tests.base.BaseTest;
import com.employee.tests.config.TestConfig;
import com.employee.tests.pages.HomePage;
import com.employee.tests.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("Employee Management")
@Feature("Home Page")
public class HomePageTest extends BaseTest {

    @BeforeMethod
    public void login() {
        new LoginPage(driver).open().login(TestConfig.username(), TestConfig.password());
    }

    @Test(description = "Verify home page loads after login")
    @Description("Smoke test: home page title and navigation are visible")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldDisplayHomePage() {
        HomePage homePage = new HomePage(driver).open();
        Assert.assertTrue(homePage.isLoaded(), "Home page should be loaded");
        Assert.assertEquals(homePage.getTitleText(), "Welcome to Employee Management");
    }
}
