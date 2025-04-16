package Tests;

import Drivers.DriverManager;
import Listeners.TestNGListeners;
import Pages.HomePage;
import Utils.BrowserActions;
import Utils.CookieUtils;
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
@Feature("Search")
@Listeners(TestNGListeners.class)
public class SearchTest {

    //Variables
    WebDriver driver;
    JsonUtils loginData = new JsonUtils("auth-data");
    JsonUtils searchData = new JsonUtils("search-data");
    String password = loginData.getJsonData("user.password");
    Set<Cookie> cookies;

    //Methods
    @BeforeClass
    public void beforeClass() {
        String email = "Abdelrahman-" + TimestampUtils.getTimestamp() + "@gmail.com";
        createAccount(loginData.getJsonData("user.firstname"), loginData.getJsonData("user.lastname"), email, password);
        DriverManager.setupDriver();
        driver = DriverManager.getDriver();
        driver.get(getPropertyValue("BASE_URL"));
        new HomePage(driver).NavigateToLoginPage()
                .enterEmail(email)
                .enterPassword(password)
                .clickOnLoginButton();
        cookies = CookieUtils.getCookies(driver);
        DriverManager.quit();
    }

    @BeforeMethod
    public void setup() {
        DriverManager.setupDriver();
        driver = DriverManager.getDriver();
        driver.get(getPropertyValue("BASE_URL"));
        CookieUtils.addCookies(driver, cookies);
        BrowserActions.refreshPage(driver);
    }

    @Test(priority = 1, description = "Search for a product")
    @Description("Verify that the user can search for a product using the search bar")
    @Severity(SeverityLevel.CRITICAL)
    public void searchForProduct() {
        new HomePage(driver)
                .searchForProduct(searchData.getJsonData("product.Existent"))
                .assertSearchResultsAreCorrect(searchData.getJsonData("product.Existent"));
    }

    @Test(priority = 2, description = "Search for non-existent product")
    @Description("Verify that the user sees a message when searching for non-existent product")
    @Severity(SeverityLevel.NORMAL)
    public void searchForNonExistentProduct() {
        new HomePage(driver)
                .searchForProduct(searchData.getJsonData("product.nonExistent"))
                .assertNoResultsFound(searchData.getJsonData("message.nonExistentMessage"));
    }

    @AfterMethod
    public void closeBrowser() {
        DriverManager.quit();
    }
}
