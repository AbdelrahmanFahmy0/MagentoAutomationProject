package Tests;

import Drivers.DriverManager;
import Listeners.TestNGListeners;
import Pages.HomePage;
import Utils.JsonUtils;
import Utils.TimestampUtils;
import io.qameta.allure.*;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.util.Set;

import static Apis.CreateAccount.createAccount;
import static Utils.PropertiesUtils.getPropertyValue;

@Owner("Abdelrahman Fahmy")
@Epic("Website")
@Feature("Logout")
@Listeners(TestNGListeners.class)
public class LogoutTest {

    //Variables
    WebDriver driver;
    JsonUtils loginData = new JsonUtils("auth-data");
    String email = "Abdelrahman-" + TimestampUtils.getTimestamp() + "@gmail.com";
    String password = loginData.getJsonData("user.password");
    Set<Cookie> cookies;

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
        new HomePage(driver).NavigateToLoginPage()
                .enterEmail(email)
                .enterPassword(password)
                .clickOnLoginButton();
    }

    @Test(description = "Valid User Logout")
    @Description("Verify that user can log out successfully")
    @Severity(SeverityLevel.CRITICAL)
    public void validUserLogout() {
        new HomePage(driver)
                .clickOnLogoutLink()
                .assertLogoutMessage(loginData.getJsonData("messages.logoutMessage"));
    }

    @AfterMethod
    public void closeBrowser() {
        DriverManager.quit();
    }
}
