package Tests;

import Drivers.DriverManager;
import Pages.HomePage;
import Pages.LoginPage;
import Utils.JsonUtils;
import Listeners.TestNGListeners;
import Utils.TimestampUtils;
import io.qameta.allure.*;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import static Apis.CreateAccount.createAccount;
import static Utils.PropertiesUtils.getPropertyValue;

@Owner("Abdelrahman Fahmy")
@Epic("Website")
@Feature("Login")
@Listeners(TestNGListeners.class)
public class LoginTest {

    //Variables
    WebDriver driver;
    JsonUtils loginData = new JsonUtils("auth-data");
    String email = "Abdelrahman-" + TimestampUtils.getTimestamp() + "@gmail.com";
    String password = loginData.getJsonData("user.password");

    //Methods
    @BeforeClass
    public void beforeClass() {
        createAccount(loginData.getJsonData("user.firstname"), loginData.getJsonData("user.lastname"), email, password);
    }

    @BeforeMethod
    public void setup() {
        DriverManager.setupDriver();
        driver = DriverManager.getDriver();
        driver.get(getPropertyValue("BASE_URL"));
    }

    @Test(priority = 1, description = "Valid User Login")
    @Description("Verify that a user can successfully login with a valid data.")
    @Severity(SeverityLevel.BLOCKER)
    public void validLoginTC() {
        new HomePage(driver).NavigateToLoginPage()
                .assertLoginPageUrl(getPropertyValue("LOGIN_URL"))
                .enterEmail(email)
                .enterPassword(password)
                .clickOnLoginButton()
                .assertHomePageUrl(getPropertyValue("BASE_URL"))
                .assertNameInHeader(loginData.getJsonData("user.firstname") + " " + loginData.getJsonData("user.lastname"));
    }

    @Test(priority = 2, description = "Login with Invalid Password")
    @Description("Verify that a user cannot login with an invalid password.")
    @Severity(SeverityLevel.CRITICAL)
    public void loginWithInvalidPasswordTC() {
        new HomePage(driver)
                .NavigateToLoginPage()
                .assertLoginPageUrl(getPropertyValue("LOGIN_URL"))
                .enterEmail(email)
                .enterPassword(loginData.getJsonData("inValidaData.password"))
                .clickOnLoginButton();
        new LoginPage(driver)
                .assertAlertMessage(loginData.getJsonData("messages.invalidLoginMessage"));
    }

    @Test(priority = 2, description = "Login with Non-Existent Email")
    @Description("Verify that a user cannot login with a non-existent email.")
    @Severity(SeverityLevel.CRITICAL)
    public void loginWithNonExistentEmailTC() {
        new HomePage(driver)
                .NavigateToLoginPage()
                .assertLoginPageUrl(getPropertyValue("LOGIN_URL"))
                .enterEmail(loginData.getJsonData("inValidaData.email"))
                .enterPassword(password)
                .clickOnLoginButton();
        new LoginPage(driver)
                .assertAlertMessage(loginData.getJsonData("messages.invalidLoginMessage"));
    }

    @Test(priority = 2, description = "Login with Empty Email")
    @Description("Verify that a user cannot login with an empty email.")
    @Severity(SeverityLevel.NORMAL)
    public void loginWithEmptyEmailTC() {
        new HomePage(driver)
                .NavigateToLoginPage()
                .assertLoginPageUrl(getPropertyValue("LOGIN_URL"))
                .enterPassword(password)
                .clickOnLoginButton();
        new LoginPage(driver)
                .assertEmailErrorMessage(loginData.getJsonData("messages.requiredFieldMessage"));
    }

    @Test(priority = 2, description = "Login with Empty Password")
    @Description("Verify that a user cannot login with an empty password.")
    @Severity(SeverityLevel.NORMAL)
    public void loginWithEmptyPasswordTC() {
        new HomePage(driver)
                .NavigateToLoginPage()
                .assertLoginPageUrl(getPropertyValue("LOGIN_URL"))
                .enterEmail(email)
                .clickOnLoginButton();
        new LoginPage(driver)
                .assertPasswordErrorMessage(loginData.getJsonData("messages.requiredFieldMessage"));
    }

    @Test(priority = 2, description = "Login with Empty Data")
    @Description("Verify that a user cannot login with empty data.")
    @Severity(SeverityLevel.NORMAL)
    public void loginWithEmptyDataTC() {
        new HomePage(driver)
                .NavigateToLoginPage()
                .assertLoginPageUrl(getPropertyValue("LOGIN_URL"))
                .clickOnLoginButton();
        new LoginPage(driver)
                .assertEmailErrorMessage(loginData.getJsonData("messages.requiredFieldMessage"))
                .assertPasswordErrorMessage(loginData.getJsonData("messages.requiredFieldMessage"));
    }

    @Test(priority = 2, description = "Login with Invalid Case-Sensitive Password")
    @Description("Verify that a user cannot login with an invalid case-sensitive password.")
    @Severity(SeverityLevel.MINOR)
    public void loginWithInvalidCaseSensitivePasswordTC() {
        new HomePage(driver)
                .NavigateToLoginPage()
                .assertLoginPageUrl(getPropertyValue("LOGIN_URL"))
                .enterEmail(email)
                .enterPassword(password.toUpperCase())
                .clickOnLoginButton();
        new LoginPage(driver)
                .assertAlertMessage(loginData.getJsonData("messages.invalidLoginMessage"));
    }

    @Test(priority = 2, description = "Login with Invalid Email Format")
    @Description("Verify that a user cannot login with an invalid email format.")
    @Severity(SeverityLevel.MINOR)
    public void loginWithInvalidEmailFormatTC() {
        new HomePage(driver)
                .NavigateToLoginPage()
                .assertLoginPageUrl(getPropertyValue("LOGIN_URL"))
                .enterEmail(loginData.getJsonData("inValidaData.invalidEmailWithout@"))
                .enterPassword(password)
                .clickOnLoginButton();
        new LoginPage(driver)
                .assertEmailErrorMessage(loginData.getJsonData("messages.invalidEmailMessage"));
    }

    @Test(priority = 2, description = "Login with Invalid Email Domain")
    @Description("Verify that a user cannot login with an invalid email domain.")
    @Severity(SeverityLevel.MINOR)
    public void loginWithInvalidEmailDomainTC() {
        new HomePage(driver)
                .NavigateToLoginPage()
                .assertLoginPageUrl(getPropertyValue("LOGIN_URL"))
                .enterEmail(loginData.getJsonData("inValidaData.invalidEmailWithoutDomain"))
                .enterPassword(password)
                .clickOnLoginButton();
        new LoginPage(driver)
                .assertEmailErrorMessage(loginData.getJsonData("messages.invalidEmailMessage"));
    }

    @AfterMethod
    public void closeBrowser() {
        DriverManager.quit();
    }
}
