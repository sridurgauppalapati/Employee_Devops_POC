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
import org.testng.annotations.Test;

@Epic("Employee Management")
@Feature("Login")
public class LoginTest extends BaseTest {

    @Test(description = "Verify user can login with valid credentials")
    @Description("Smoke test: valid login redirects to home page")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldLoginWithValidCredentials() {
        LoginPage loginPage = new LoginPage(driver).open();
        Assert.assertTrue(loginPage.isDisplayed(), "Login page should be displayed");

        loginPage.login(TestConfig.username(), TestConfig.password());

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isLoaded(), "Home page should load after successful login");
    }

    @Test(description = "Verify invalid login shows error")
    @Description("Smoke test: invalid credentials show login error")
    @Severity(SeverityLevel.NORMAL)
    public void shouldShowErrorForInvalidCredentials() {
        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.login("invalid-user", "invalid-password");

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Login error message should be displayed");
    }
}
