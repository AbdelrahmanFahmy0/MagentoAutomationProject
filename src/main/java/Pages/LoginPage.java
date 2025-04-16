package Pages;

import Utils.BrowserActions;
import Utils.ElementActions;
import Utils.Validations;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    //Variables
    private final WebDriver driver;

    //Locators
    private final By email = By.id("email");
    private final By password = By.xpath("//fieldset//input[@type='password']");
    private final By loginButton = By.xpath("//fieldset//button");
    private final By emailError = By.id("email-error");
    private final By passwordError = By.id("pass-error");
    private final By alert = By.xpath("//div[@role='alert']/div/div");

    //Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    //Actions
    @Step("Enter email: {0}")
    public LoginPage enterEmail(String emailText) {
        ElementActions.sendData(driver, email, emailText);
        return this;
    }

    @Step("Enter password: {0}")
    public LoginPage enterPassword(String passwordText) {
        ElementActions.sendData(driver, password, passwordText);
        return this;
    }

    @Step("Click on login button")
    public HomePage clickOnLoginButton() {
        ElementActions.clickElement(driver, loginButton);
        return new HomePage(driver);
    }


    //Assertions
    @Step("Assert login page url")
    public LoginPage assertLoginPageUrl(String expectedUrl) {
        String currentUrl = BrowserActions.getCurrentURL(driver);
        Validations.validateTrue(currentUrl.contains(expectedUrl), "URL is not as expected");
        return this;
    }

    @Step("Assert alert message")
    public LoginPage assertAlertMessage(String expectedAlertMessage) {
        String actualAlertMessage = ElementActions.getText(driver, alert);
        Validations.validateEquals(actualAlertMessage, expectedAlertMessage, "Alert message is not as expected");
        return this;
    }

    @Step("Assert email error message")
    public LoginPage assertEmailErrorMessage(String expectedEmailErrorMessage) {
        String actualEmailErrorMessage = ElementActions.getText(driver, emailError);
        Validations.validateEquals(actualEmailErrorMessage, expectedEmailErrorMessage, "Email error message is not as expected");
        return this;
    }

    @Step("Assert password error message")
    public LoginPage assertPasswordErrorMessage(String expectedPasswordErrorMessage) {
        String actualPasswordErrorMessage = ElementActions.getText(driver, passwordError);
        Validations.validateEquals(actualPasswordErrorMessage, expectedPasswordErrorMessage, "Password error message is not as expected");
        return this;
    }
}
