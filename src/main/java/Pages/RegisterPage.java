package Pages;

import Utils.BrowserActions;
import Utils.ElementActions;
import Utils.Validations;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPage {

    //Variables
    private final WebDriver driver;

    //Locators
    private final By firstName = By.id("firstname");
    private final By lastName = By.id("lastname");
    private final By email = By.id("email_address");
    private final By password = By.id("password");
    private final By confirmPassword = By.id("password-confirmation");
    private final By createAccountButton = By.xpath("//button[@title='Create an Account']");
    private final By firstNameError = By.id("firstname-error");
    private final By lastNameError = By.id("lastname-error");
    private final By emailError = By.id("email_address-error");
    private final By passwordError = By.id("password-error");
    private final By confirmPasswordError = By.id("password-confirmation-error");
    private final By passwordStrength = By.id("password-strength-meter-label");
    private final By alert = By.xpath("//div[@role='alert']/div/div");

    //Constructor
    public RegisterPage(WebDriver driver) {
        this.driver = driver;
    }

    //Actions
    @Step("Enter first name: {0}")
    public RegisterPage enterFirstName(String firstNameText) {
        ElementActions.sendData(driver, firstName, firstNameText);
        return this;
    }

    @Step("Enter last name: {0}")
    public RegisterPage enterLastName(String lastNameText) {
        ElementActions.sendData(driver, lastName, lastNameText);
        return this;
    }

    @Step("Enter email: {0}")
    public RegisterPage enterEmail(String emailText) {
        ElementActions.sendData(driver, email, emailText);
        return this;
    }

    @Step("Enter password: {0}")
    public RegisterPage enterPassword(String passwordText) {
        ElementActions.sendData(driver, password, passwordText);
        return this;
    }

    @Step("Enter confirm password: {0}")
    public RegisterPage enterConfirmPassword(String confirmPasswordText) {
        ElementActions.sendData(driver, confirmPassword, confirmPasswordText);
        return this;
    }

    @Step("Fill the registration form")
    public RegisterPage fillRegistrationForm(String firstNameText, String lastNameText, String emailText, String passwordText, String confirmPasswordText) {
        enterFirstName(firstNameText);
        enterLastName(lastNameText);
        enterEmail(emailText);
        enterPassword(passwordText);
        enterConfirmPassword(confirmPasswordText);
        return this;
    }

    @Step("Click on create account button")
    public MyAccountPage clickOnCreateAccountButton() {
        ElementActions.clickElement(driver, createAccountButton);
        return new MyAccountPage(driver);
    }

    //Assertions
    @Step("Assert the page url")
    public RegisterPage assertRegisterPageUrl(String expectedUrl) {
        String currentUrl = BrowserActions.getCurrentURL(driver);
        Validations.validateEquals(currentUrl, expectedUrl, "URL is not as expected");
        return this;
    }

    @Step("Assert the password strength")
    public RegisterPage assertPasswordStrength(String expectedStrength) {
        String actualStrength = ElementActions.getText(driver, passwordStrength);
        Validations.validateEquals(actualStrength, expectedStrength, "Password strength is not as expected");
        return this;
    }

    @Step("Assert first name error message")
    public RegisterPage assertFirstNameErrorMessage(String expectedErrorMessage) {
        String actualErrorMessage = ElementActions.getText(driver, firstNameError);
        Validations.validateEquals(actualErrorMessage, expectedErrorMessage, "First name error message is not as expected");
        return this;
    }

    @Step("Assert last name error message")
    public RegisterPage assertLastNameErrorMessage(String expectedErrorMessage) {
        String actualErrorMessage = ElementActions.getText(driver, lastNameError);
        Validations.validateEquals(actualErrorMessage, expectedErrorMessage, "Last name error message is not as expected");
        return this;
    }

    @Step("Assert email error message")
    public RegisterPage assertEmailErrorMessage(String expectedErrorMessage) {
        String actualErrorMessage = ElementActions.getText(driver, emailError);
        Validations.validateEquals(actualErrorMessage, expectedErrorMessage, "Email error message is not as expected");
        return this;
    }

    @Step("Assert password error message")
    public RegisterPage assertPasswordErrorMessage(String expectedErrorMessage) {
        String actualErrorMessage = ElementActions.getText(driver, passwordError);
        Validations.validateEquals(actualErrorMessage, expectedErrorMessage, "Password error message is not as expected");
        return this;
    }

    @Step("Assert confirm password error message")
    public RegisterPage assertConfirmPasswordErrorMessage(String expectedErrorMessage) {
        String actualErrorMessage = ElementActions.getText(driver, confirmPasswordError);
        Validations.validateEquals(actualErrorMessage, expectedErrorMessage, "Confirm password error message is not as expected");
        return this;
    }

    @Step("Assert alert message")
    public RegisterPage assertAlertMessage(String expectedAlertMessage) {
        String actualAlertMessage = ElementActions.getText(driver, alert);
        Boolean result = actualAlertMessage.contains(expectedAlertMessage);
        Validations.validateTrue(result, "Alert message is not as expected");
        return this;
    }
}
