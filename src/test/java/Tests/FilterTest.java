package Tests;

import Drivers.DriverManager;
import Pages.HomePage;
import Utils.BrowserActions;
import Utils.CookieUtils;
import Utils.JsonUtils;
import Utils.TimestampUtils;
import io.qameta.allure.*;
import Listeners.TestNGListeners;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.util.Set;

import static Apis.CreateAccount.createAccount;
import static Utils.PropertiesUtils.getPropertyValue;

@Owner("Abdelrahman Fahmy")
@Epic("Website")
@Feature("Filter")
@Listeners(TestNGListeners.class)
public class FilterTest {

    //Variables
    WebDriver driver;
    JsonUtils loginData = new JsonUtils("auth-data");
    JsonUtils filterData = new JsonUtils("filters-data");
    String password = loginData.getJsonData("user.password");
    String mainCategory = filterData.getJsonData("category.mainCategory");
    String subCategory = filterData.getJsonData("category.subCategory");
    String targetCategory = filterData.getJsonData("category.targetCategory");
    String style = filterData.getJsonData("style");
    String size = filterData.getJsonData("size");
    String lowPrice = filterData.getJsonData("price.lowPrice");
    String highPrice = filterData.getJsonData("price.highPrice");
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

    @Test(priority = 1, description = "Filter by Category")
    @Description("Verify that the user can filter products by category")
    @Severity(SeverityLevel.CRITICAL)
    public void filterByCategoryTC() {
        new HomePage(driver)
                .selectCategory(mainCategory, subCategory, targetCategory)
                .assertCategorySelected(mainCategory, subCategory, targetCategory)
                .selectFirstProduct()
                .assertProductCategory(targetCategory);
    }

    @Test(priority = 2, description = "Filter by Style")
    @Description("Verify that the user can filter products by style")
    @Severity(SeverityLevel.NORMAL)
    public void filterByStyleTC() {
        new HomePage(driver)
                .selectCategory(mainCategory, subCategory, targetCategory)
                .selectStyle(style)
                .selectFirstProduct()
                .clickOnMoreInformation()
                .assertProductStyle(style);
    }

    @Test(priority = 2, description = "Filter by Size")
    @Description("Verify that the user can filter products by size")
    @Severity(SeverityLevel.NORMAL)
    public void filterBySizeTC() {
        new HomePage(driver)
                .selectCategory(mainCategory, subCategory, targetCategory)
                .selectSize(size)
                .selectFirstProduct()
                .assertProductSize(size);
    }

    @Test(priority = 2, description = "Filter by Price")
    @Description("Verify that the user can filter products by price")
    @Severity(SeverityLevel.NORMAL)
    public void filterByPriceTC() {
        new HomePage(driver)
                .selectCategory(mainCategory, subCategory, targetCategory)
                .selectPriceRange(lowPrice, highPrice)
                .selectFirstProduct()
                .assertProductPrice(lowPrice, highPrice);
    }

    @AfterMethod
    public void closeBrowser() {
        DriverManager.quit();
    }
}
