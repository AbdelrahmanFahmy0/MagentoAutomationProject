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
@Feature("Sort")
@Listeners(TestNGListeners.class)
public class SortTest {

    //Variables
    WebDriver driver;
    JsonUtils loginData = new JsonUtils("auth-data");
    JsonUtils filterData = new JsonUtils("filters-data");
    String password = loginData.getJsonData("user.password");
    String mainCategory = filterData.getJsonData("category.mainCategory");
    String subCategory = filterData.getJsonData("category.subCategory");
    String targetCategory = filterData.getJsonData("category.targetCategory");
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

    @Test(priority = 1, description = "Sort by Price Low to High")
    @Description("Verify that the products are sorted by price from low to high.")
    @Severity(SeverityLevel.NORMAL)
    public void sortByPriceLowToHighTC() {
        new HomePage(driver)
                .selectCategory(mainCategory, subCategory, targetCategory)
                .selectSortByPrice()
                .assertProductsSortedAscendingByPrice();
    }

    @Test(priority = 2, description = "Sort by Price High to Low")
    @Description("Verify that the products are sorted by price from high to low.")
    @Severity(SeverityLevel.NORMAL)
    public void sortByPriceHighToLowTC() {
        new HomePage(driver)
                .selectCategory(mainCategory, subCategory, targetCategory)
                .selectSortByPrice()
                .changeSortingOrder()
                .assertProductsSortedDescendingByPrice();
    }

    @AfterMethod
    public void closeBrowser() {
        DriverManager.quit();
    }
}
