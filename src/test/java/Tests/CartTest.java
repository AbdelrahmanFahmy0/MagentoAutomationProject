package Tests;

import Drivers.DriverManager;
import Listeners.TestNGListeners;
import Pages.HomePage;
import Pages.WishListPage;
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
@Feature("Add to Cart")
@Listeners(TestNGListeners.class)
public class CartTest {

    //Variables
    WebDriver driver;
    JsonUtils loginData = new JsonUtils("auth-data");
    JsonUtils filterData = new JsonUtils("filters-data");
    JsonUtils cartData = new JsonUtils("cart-data");
    JsonUtils productData = new JsonUtils("products-data");
    JsonUtils wishListData = new JsonUtils("wishlist-data");
    String password = loginData.getJsonData("user.password");
    String mainCategory = filterData.getJsonData("category.mainCategory");
    String subCategory = filterData.getJsonData("category.subCategory");
    String targetCategory = filterData.getJsonData("category.targetCategory");
    String productName = productData.getJsonData("products[0].name");
    String productPrice = productData.getJsonData("products[0].price");
    String productSize = productData.getJsonData("products[0].size");
    String productColor = productData.getJsonData("products[0].color");
    String productQuantity = productData.getJsonData("products[0].quantity");
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

    @Test(priority = 1, description = "Add Product to Cart ")
    @Description("Verify that user can add product to cart")
    @Severity(SeverityLevel.BLOCKER)
    public void addProductToCartTC() {
        new HomePage(driver)
                .selectCategory(mainCategory, subCategory, targetCategory)
                .viewProduct(productName)
                .assertProductDetails(productName, productPrice)
                .chooseProductSize(productSize)
                .chooseProductColor(productColor)
                .enterProductQuantity(productQuantity)
                .clickOnAddToCartButton()
                .assertProductAddedToCart(productQuantity)
                .navigateToCartPage()
                .assertCartPageUrl(getPropertyValue("CART_URL"))
                .assertProductDetailsInCart(productName, productPrice, productSize, productColor, productQuantity)
                .assertOrderSummary();
    }

    @Test(priority = 1, dependsOnMethods = "addProductToCartTC", description = "Remove Product from Cart")
    @Description("Verify that user can remove product from cart")
    @Severity(SeverityLevel.CRITICAL)
    public void removeProductFromCartTC() {
        new HomePage(driver)
                .navigateToCartPage()
                .assertCartPageUrl(getPropertyValue("CART_URL"))
                .removeProductFromCart(productName)
                .assertCartIsCleared(cartData.getJsonData("messages.cleanCartMessage"));
    }

    @Test(priority = 1, dependsOnMethods = "removeProductFromCartTC", description = "Checking if The Cart is Empty")
    @Description("Check if the cart is empty")
    @Severity(SeverityLevel.NORMAL)
    public void checkEmptyCartTC() {
        new HomePage(driver)
                .navigateToCartPage()
                .assertEmptyCartMessage(cartData.getJsonData("messages.emptyCartMessage"));
    }

    @Test(priority = 2, description = "Add Product to Cart without Options")
    @Description("Verify that user can't add product to cart without choosing options")
    @Severity(SeverityLevel.CRITICAL)
    public void addProductToCartWithoutOptionsTC() {
        new HomePage(driver)
                .selectCategory(mainCategory, subCategory, targetCategory)
                .addProductToCart()
                .assertNoOptionsMessage(cartData.getJsonData("messages.noOptionsMessage"));
    }

    @Test(priority = 2, description = "Add Product to Cart without Size")
    @Description("Verify that user can't add product to cart without choosing size")
    @Severity(SeverityLevel.CRITICAL)
    public void addProductToCartWithoutSizeTC() {
        new HomePage(driver)
                .selectCategory(mainCategory, subCategory, targetCategory)
                .viewProduct(productName)
                .assertProductDetails(productName, productPrice)
                .chooseProductColor(productColor)
                .enterProductQuantity(productQuantity)
                .clickOnAddToCartButton()
                .assertSizeErrorMessage(productData.getJsonData("messages.requiredFieldMessage"));
    }

    @Test(priority = 2, description = "Add Product to Cart without Color")
    @Description("Verify that user can't add product to cart without choosing color")
    @Severity(SeverityLevel.CRITICAL)
    public void addProductToCartWithoutColorTC() {
        new HomePage(driver)
                .selectCategory(mainCategory, subCategory, targetCategory)
                .viewProduct(productName)
                .assertProductDetails(productName, productPrice)
                .chooseProductSize(productSize)
                .enterProductQuantity(productQuantity)
                .clickOnAddToCartButton()
                .assertColorErrorMessage(productData.getJsonData("messages.requiredFieldMessage"));
    }

    @Test(priority = 2, description = "Add Product to Cart without Quantity")
    @Description("Verify that user can't add product to cart without entering quantity")
    @Severity(SeverityLevel.CRITICAL)
    public void addProductToCartWithoutQuantityTC() {
        new HomePage(driver)
                .selectCategory(mainCategory, subCategory, targetCategory)
                .viewProduct(productName)
                .assertProductDetails(productName, productPrice)
                .chooseProductSize(productSize)
                .chooseProductColor(productColor)
                .enterProductQuantity("")
                .clickOnAddToCartButton()
                .assertQuantityErrorMessage(productData.getJsonData("messages.invalidNumberMessage"));
    }

    @Test(priority = 2, description = "Add Product to Cart with 0 Quantity")
    @Description("Verify that user can't add product to cart with 0 quantity")
    @Severity(SeverityLevel.CRITICAL)
    public void addProductToCartWithZeroQuantityTC() {
        new HomePage(driver)
                .selectCategory(mainCategory, subCategory, targetCategory)
                .viewProduct(productName)
                .assertProductDetails(productName, productPrice)
                .chooseProductSize(productSize)
                .chooseProductColor(productColor)
                .enterProductQuantity("0")
                .clickOnAddToCartButton()
                .assertQuantityErrorMessage(productData.getJsonData("messages.invalidQuantityMessage"));
    }

    @Test(priority = 3, description = "Add Product to Cart from WishList")
    @Description("Verify that user can add product to cart from wishlist")
    @Severity(SeverityLevel.NORMAL)
    public void addProductToCartFromWishListTC() {
        new HomePage(driver)
                .selectCategory(mainCategory, subCategory, targetCategory)
                .addProductToWishlist(productName)
                .addProductToCart()
                .chooseProductSize(productSize)
                .chooseProductColor(productColor)
                .enterProductQuantity(productQuantity)
                .clickOnAddToCartButton();
        new WishListPage(driver)
                .assertEmptyWishList(wishListData.getJsonData("messages.emptyWishlistMessage"));
    }

    @AfterMethod
    public void closeBrowser() {
        DriverManager.quit();
    }
}
