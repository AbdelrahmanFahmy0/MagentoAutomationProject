package Tests;

import Drivers.DriverManager;
import Listeners.TestNGListeners;
import Pages.HomePage;
import Pages.RegisterPage;
import Utils.JsonUtils;
import Utils.TimestampUtils;
import io.qameta.allure.*;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static Utils.PropertiesUtils.getPropertyValue;

@Owner("Abdelrahman Fahmy")
@Epic("Website")
@Feature("Register")
@Listeners(TestNGListeners.class)
public class RegisterTest {

    //Variables
    WebDriver driver;
    JsonUtils registerData = new JsonUtils("auth-data");
    String firstName = registerData.getJsonData("user.firstname");
    String lastName = registerData.getJsonData("user.lastname");
    String email = "Abdelrahman-" + TimestampUtils.getTimestamp() + "@gmail.com";
    String password = registerData.getJsonData("user.password");
    String confirmPassword = registerData.getJsonData("user.password");

    //Methods
    @BeforeMethod
    public void setUp() {
        DriverManager.setupDriver();
        driver = DriverManager.getDriver();
        driver.get(getPropertyValue("BASE_URL"));
    }

    @Test(priority = 1, description = "Valid User Registration")
    @Description("Verify that a user can successfully register with a valid data.")
    @Severity(SeverityLevel.BLOCKER)
    public void validRegistrationTC() {
        new HomePage(driver).NavigateToRegisterPage()
                .assertRegisterPageUrl(getPropertyValue("REGISTRATION_URL"))
                .fillRegistrationForm(firstName, lastName, email, password, confirmPassword)
                .assertPasswordStrength(registerData.getJsonData("passwordLevels[2]"))
                .clickOnCreateAccountButton()
                .assertMyAccountPageUrl(getPropertyValue("MY_ACCOUNT_URL"))
                .assertNameInHeader(firstName + " " + lastName)
                .assertConfirmationMessage(registerData.getJsonData("messages.successRegisterMessage"));
    }

    @Test(dependsOnMethods = "validRegistrationTC", priority = 2, description = "Register with Existing Email")
    @Description("Verify that the system prevents registration using an already existing email.")
    @Severity(SeverityLevel.CRITICAL)
    public void registerWithExistingEmailTC() {
        new HomePage(driver).NavigateToRegisterPage()
                .assertRegisterPageUrl(getPropertyValue("REGISTRATION_URL"))
                .fillRegistrationForm(firstName, lastName, email, password, confirmPassword)
                .clickOnCreateAccountButton();
        new RegisterPage(driver)
                .assertAlertMessage(registerData.getJsonData("messages.existingEmailMessage"));
    }

    @Test(priority = 2, description = "Register with Empty First Name")
    @Description("Verify that the system prevents registration with an empty first name.")
    @Severity(SeverityLevel.CRITICAL)
    public void registerWithEmptyFirstNameTC() {
        new HomePage(driver).NavigateToRegisterPage()
                .assertRegisterPageUrl(getPropertyValue("REGISTRATION_URL"))
                .fillRegistrationForm("", lastName, email, password, confirmPassword)
                .clickOnCreateAccountButton();
        new RegisterPage(driver)
                .assertFirstNameErrorMessage(registerData.getJsonData("messages.requiredFieldMessage"));
    }

    @Test(priority = 2, description = "Register with First Name containing Special Characters")
    @Description("Verify that the system prevents registration with a first name containing special characters.")
    @Severity(SeverityLevel.NORMAL)
    public void registerWithSpecialCharactersInFirstNameTC() {
        email = "Abdelrahman-" + TimestampUtils.getTimestamp() + "@gmail.com";
        new HomePage(driver).NavigateToRegisterPage()
                .assertRegisterPageUrl(getPropertyValue("REGISTRATION_URL"))
                .fillRegistrationForm(registerData.getJsonData("inValidaData.nameWithSpecialChars"), lastName, email, password, confirmPassword)
                .clickOnCreateAccountButton();
        new RegisterPage(driver)
                .assertAlertMessage(registerData.getJsonData("messages.invalidFirstNameMessage"));
    }

    @Test(priority = 2, description = "Register with Empty Last Name")
    @Description("Verify that the system prevents registration with an empty last name.")
    @Severity(SeverityLevel.CRITICAL)
    public void registerWithEmptyLastNameTC() {
        new HomePage(driver).NavigateToRegisterPage()
                .assertRegisterPageUrl(getPropertyValue("REGISTRATION_URL"))
                .fillRegistrationForm(firstName, "", email, password, confirmPassword)
                .clickOnCreateAccountButton();
        new RegisterPage(driver)
                .assertLastNameErrorMessage(registerData.getJsonData("messages.requiredFieldMessage"));
    }

    @Test(priority = 2, description = "Register with Last Name containing Special Characters")
    @Description("Verify that the system prevents registration with a last name containing special characters.")
    @Severity(SeverityLevel.NORMAL)
    public void registerWithSpecialCharactersInLastNameTC() {
        email = "Abdelrahman-" + TimestampUtils.getTimestamp() + "@gmail.com";
        new HomePage(driver).NavigateToRegisterPage()
                .assertRegisterPageUrl(getPropertyValue("REGISTRATION_URL"))
                .fillRegistrationForm(firstName, registerData.getJsonData("inValidaData.nameWithSpecialChars"), email, password, confirmPassword)
                .clickOnCreateAccountButton();
        new RegisterPage(driver)
                .assertAlertMessage(registerData.getJsonData("messages.invalidLastNameMessage"));
    }

    @Test(priority = 2, description = "Register with Empty Email")
    @Description("Verify that the system prevents registration with an empty email.")
    @Severity(SeverityLevel.CRITICAL)
    public void registerWithEmptyEmailTC() {
        new HomePage(driver).NavigateToRegisterPage()
                .assertRegisterPageUrl(getPropertyValue("REGISTRATION_URL"))
                .fillRegistrationForm(firstName, lastName, "", password, confirmPassword)
                .clickOnCreateAccountButton();
        new RegisterPage(driver)
                .assertEmailErrorMessage(registerData.getJsonData("messages.requiredFieldMessage"));
    }

    @Test(priority = 2, description = "Register with Invalid Email Format")
    @Description("Verify that the system prevents registration with an invalid email format.")
    @Severity(SeverityLevel.NORMAL)
    public void registerWithInvalidEmailFormatTC() {
        new HomePage(driver).NavigateToRegisterPage()
                .assertRegisterPageUrl(getPropertyValue("REGISTRATION_URL"))
                .fillRegistrationForm(firstName, lastName, registerData.getJsonData("inValidaData.invalidEmailWithout@"), password, confirmPassword)
                .clickOnCreateAccountButton();
        new RegisterPage(driver)
                .assertEmailErrorMessage(registerData.getJsonData("messages.invalidEmailMessage"));
    }

    @Test(priority = 2, description = "Register with Invalid Email Domain")
    @Description("Verify that the system prevents registration with an invalid email domain.")
    @Severity(SeverityLevel.NORMAL)
    public void registerWithInvalidEmailDomainTC() {
        new HomePage(driver).NavigateToRegisterPage()
                .assertRegisterPageUrl(getPropertyValue("REGISTRATION_URL"))
                .fillRegistrationForm(firstName, lastName, registerData.getJsonData("inValidaData.invalidEmailWithoutDomain"), password, confirmPassword)
                .clickOnCreateAccountButton();
        new RegisterPage(driver)
                .assertEmailErrorMessage(registerData.getJsonData("messages.invalidEmailMessage"));
    }

    @Test(priority = 2, description = "Register with Empty Password")
    @Description("Verify that the system prevents registration with an empty password.")
    @Severity(SeverityLevel.CRITICAL)
    public void registerWithEmptyPasswordTC() {
        new HomePage(driver).NavigateToRegisterPage()
                .assertRegisterPageUrl(getPropertyValue("REGISTRATION_URL"))
                .fillRegistrationForm(firstName, lastName, email, "", confirmPassword)
                .clickOnCreateAccountButton();
        new RegisterPage(driver)
                .assertPasswordErrorMessage(registerData.getJsonData("messages.requiredFieldMessage"));
    }

    @Test(priority = 2, description = "Register with Password shorter than 8 characters")
    @Description("Verify that the system prevents registration with a password shorter than 8 characters.")
    @Severity(SeverityLevel.NORMAL)
    public void registerWithShortPasswordTC() {
        new HomePage(driver).NavigateToRegisterPage()
                .assertRegisterPageUrl(getPropertyValue("REGISTRATION_URL"))
                .fillRegistrationForm(firstName, lastName, email, registerData.getJsonData("inValidaData.weakPassword"), confirmPassword)
                .assertPasswordStrength(registerData.getJsonData("passwordLevels[0]"))
                .assertPasswordErrorMessage(registerData.getJsonData("messages.invalidPasswordLengthMessage"))
                .clickOnCreateAccountButton();
    }

    @Test(priority = 2, description = "Register with Invalid Password Format")
    @Description("Verify that the system prevents registration with invalid password format.")
    @Severity(SeverityLevel.NORMAL)
    public void registerWithInvalidPasswordFormat() {
        new HomePage(driver).NavigateToRegisterPage()
                .assertRegisterPageUrl(getPropertyValue("REGISTRATION_URL"))
                .fillRegistrationForm(firstName, lastName, email, registerData.getJsonData("inValidaData.invalidPasswordFormat"), registerData.getJsonData("inValidaData.invalidPasswordFormat"))
                .assertPasswordStrength(registerData.getJsonData("passwordLevels[0]"))
                .assertPasswordErrorMessage(registerData.getJsonData("messages.invalidPasswordFormatMessage"))
                .clickOnCreateAccountButton();
    }

    @Test(priority = 2, description = "Register with Medium Password")
    @Description("Verify that the system accepts registration with medium password strength.")
    @Severity(SeverityLevel.CRITICAL)
    public void registerWithMediumPassword() {
        email = "Abdelrahman-" + TimestampUtils.getTimestamp() + "@gmail.com";
        new HomePage(driver).NavigateToRegisterPage()
                .assertRegisterPageUrl(getPropertyValue("REGISTRATION_URL"))
                .fillRegistrationForm(firstName, lastName, email, registerData.getJsonData("inValidaData.mediumPassword"), registerData.getJsonData("inValidaData.mediumPassword"))
                .assertPasswordStrength(registerData.getJsonData("passwordLevels[1]"))
                .clickOnCreateAccountButton()
                .assertMyAccountPageUrl(getPropertyValue("MY_ACCOUNT_URL"))
                .assertNameInHeader(firstName + " " + lastName)
                .assertConfirmationMessage(registerData.getJsonData("messages.successRegisterMessage"));
    }

    @Test(priority = 2, description = "Register with Empty Confirm Password")
    @Description("Verify that the system prevents registration with an empty confirm password.")
    @Severity(SeverityLevel.CRITICAL)
    public void registerWithEmptyConfirmPasswordTC() {
        new HomePage(driver).NavigateToRegisterPage()
                .assertRegisterPageUrl(getPropertyValue("REGISTRATION_URL"))
                .fillRegistrationForm(firstName, lastName, email, password, "")
                .clickOnCreateAccountButton();
        new RegisterPage(driver)
                .assertConfirmPasswordErrorMessage(registerData.getJsonData("messages.requiredFieldMessage"));
    }

    @Test(priority = 2, description = "Register with Non-Matched Confirm Password")
    @Description("Verify that the system prevents registration with confirm password that doesn't match password.")
    @Severity(SeverityLevel.CRITICAL)
    public void registerWithNonMatchedConfirmPasswordTC() {
        new HomePage(driver).NavigateToRegisterPage()
                .assertRegisterPageUrl(getPropertyValue("REGISTRATION_URL"))
                .fillRegistrationForm(firstName, lastName, email, password, registerData.getJsonData("inValidaData.password"))
                .clickOnCreateAccountButton();
        new RegisterPage(driver).assertConfirmPasswordErrorMessage(registerData.getJsonData("messages.differentPasswordMessage"));
    }

    @Test(priority = 3, description = "Register with Empty Data")
    @Description("Verify that the system prevents registration with empty data.")
    @Severity(SeverityLevel.NORMAL)
    public void registerWithEmptyDataTC() {
        new HomePage(driver).NavigateToRegisterPage()
                .assertRegisterPageUrl(getPropertyValue("REGISTRATION_URL"))
                .clickOnCreateAccountButton();
        new RegisterPage(driver)
                .assertFirstNameErrorMessage(registerData.getJsonData("messages.requiredFieldMessage"))
                .assertLastNameErrorMessage(registerData.getJsonData("messages.requiredFieldMessage"))
                .assertEmailErrorMessage(registerData.getJsonData("messages.requiredFieldMessage"))
                .assertPasswordErrorMessage(registerData.getJsonData("messages.requiredFieldMessage"))
                .assertConfirmPasswordErrorMessage(registerData.getJsonData("messages.requiredFieldMessage"));
    }

    @AfterMethod
    public void closeBrowser() {
        DriverManager.quit();
    }
}
