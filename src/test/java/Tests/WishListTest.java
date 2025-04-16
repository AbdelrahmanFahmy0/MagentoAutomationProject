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
@Feature("WishList")
@Listeners(TestNGListeners.class)
public class WishListTest {

    //Variables
    WebDriver driver;
    JsonUtils loginData = new JsonUtils("auth-data");
    JsonUtils filterData = new JsonUtils("filters-data");
    JsonUtils wishListData = new JsonUtils("wishlist-data");
    JsonUtils productData = new JsonUtils("products-data");
    String password = loginData.getJsonData("user.password");
    String mainCategory = filterData.getJsonData("category.mainCategory");
    String subCategory = filterData.getJsonData("category.subCategory");
    String targetCategory = filterData.getJsonData("category.targetCategory");
    String productName = productData.getJsonData("products[0].name");
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

    @Test(priority = 1, description = "Check Empty WishList")
    @Description("Check if the wishlist is empty")
    @Severity(SeverityLevel.NORMAL)
    public void checkEmptyWishListTC() {
        new HomePage(driver)
                .navigateToMyWishListPage()
                .assertWishListPageUrl(getPropertyValue("WISHLIST_URL"))
                .assertEmptyWishList(wishListData.getJsonData("messages.emptyWishlistMessage"));
    }

    @Test(priority = 2, description = "Add Product to WishList")
    @Description("Verify that the user can add a product to the wishlist")
    @Severity(SeverityLevel.NORMAL)
    public void addProductToWishListTC() {
        new HomePage(driver)
                .selectCategory(mainCategory, subCategory, targetCategory)
                .addProductToWishlist(productName)
                .assertWishListContainsProduct(productName);
    }

    @Test(priority = 2, dependsOnMethods = "addProductToWishListTC", description = "Remove Product from WishList")
    @Description("Verify that the user can remove a product from the wishlist")
    @Severity(SeverityLevel.NORMAL)
    public void removeProductFromWishListTC() {
        new HomePage(driver)
                .navigateToMyWishListPage()
                .removeProductFromWishList()
                .assertEmptyWishList(wishListData.getJsonData("messages.emptyWishlistMessage"));
    }

    @AfterMethod
    public void closeBrowser() {
        DriverManager.quit();
    }
}
