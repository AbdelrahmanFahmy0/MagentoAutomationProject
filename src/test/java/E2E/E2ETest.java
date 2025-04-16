package E2E;

import Drivers.DriverManager;
import Listeners.TestNGListeners;
import Pages.CartPage;
import Pages.HomePage;
import Pages.ProductPage;
import Utils.JsonUtils;
import Utils.TimestampUtils;
import com.github.javafaker.Faker;
import io.qameta.allure.*;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static Utils.PropertiesUtils.getPropertyValue;

@Owner("Abdelrahman Fahmy")
@Epic("Website")
@Listeners(TestNGListeners.class)
public class E2ETest {

    //Variables
    WebDriver driver;
    JsonUtils registerData = new JsonUtils("auth-data");
    String firstName = registerData.getJsonData("user.firstname");
    String lastName = registerData.getJsonData("user.lastname");
    String email = "Abdelrahman-" + TimestampUtils.getTimestamp() + "@gmail.com";
    String password = registerData.getJsonData("user.password");
    String confirmPassword = registerData.getJsonData("user.password");
    JsonUtils filterData = new JsonUtils("filters-data");
    String mainCategory = filterData.getJsonData("category.mainCategory");
    String subCategory = filterData.getJsonData("category.subCategory");
    String targetCategory = filterData.getJsonData("category.targetCategory");
    String size = filterData.getJsonData("size");
    JsonUtils productData = new JsonUtils("products-data");
    String productName = productData.getJsonData("products[0].name");
    String productPrice = productData.getJsonData("products[0].price");
    String productSize = productData.getJsonData("products[0].size");
    String productColor = productData.getJsonData("products[0].color");
    String productQuantity = productData.getJsonData("products[0].quantity");
    JsonUtils checkoutData = new JsonUtils("checkout-data");
    Faker faker = new Faker();
    String address = faker.address().fullAddress();
    String city = faker.address().cityName();
    String zipCode = faker.address().zipCode();
    String country = checkoutData.getJsonData("user.country");
    String phoneNumber = faker.phoneNumber().phoneNumber();
    String shippingPrice = checkoutData.getJsonData("shippingMethod.price");
    String shippingMethod = checkoutData.getJsonData("shippingMethod.method");

    //Methods
    @BeforeClass
    public void setUp() {
        DriverManager.setupDriver();
        driver = DriverManager.getDriver();
        driver.get(getPropertyValue("BASE_URL"));
    }

    @Test(description = "Valid User Registration")
    @Description("Verify that a user can successfully register with a valid data.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Register")
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

    @Test(dependsOnMethods = "validRegistrationTC", description = "Valid User Logout")
    @Description("Verify that user can log out successfully")
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Logout")
    public void validUserLogout() {
        new HomePage(driver)
                .clickOnLogoutLink()
                .assertLogoutMessage(registerData.getJsonData("messages.logoutMessage"));
    }

    @Test(dependsOnMethods = "validUserLogout", description = "Valid User Login")
    @Description("Verify that a user can successfully login with a valid data.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Login")
    public void validLoginTC() {
        new HomePage(driver).NavigateToLoginPage()
                .assertLoginPageUrl(getPropertyValue("LOGIN_URL"))
                .enterEmail(email)
                .enterPassword(password)
                .clickOnLoginButton()
                .assertHomePageUrl(getPropertyValue("MY_ACCOUNT_URL"))
                .assertNameInHeader(firstName + " " + lastName);
    }

    @Test(dependsOnMethods = "validLoginTC", description = "Filter by Category")
    @Description("Verify that the user can filter products by category and size")
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Filter")
    public void filterByCategoryAndSizeTC() {
        new HomePage(driver)
                .selectCategory(mainCategory, subCategory, targetCategory)
                .assertCategorySelected(mainCategory, subCategory, targetCategory)
                .selectSize(size)
                .viewProduct(productName)
                .assertProductCategory(targetCategory)
                .assertProductSize(size);

    }

    @Test(dependsOnMethods = "filterByCategoryAndSizeTC", description = "Add Product to Cart ")
    @Description("Verify that user can add product to cart")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Add to Cart")
    public void addProductToCartTC() {
        new ProductPage(driver)
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

    @Test(dependsOnMethods = "addProductToCartTC", description = "Valid Order Checkout")
    @Description("Verify that user can checkout with valid data")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Checkout")
    public void validCheckoutTC() {
        String subtotal = new CartPage(driver).getOrderSubtotalPrice();
        new CartPage(driver)
                .clickOnCheckoutButton()
                .fillShippingForm("", "", address, city, zipCode, country, phoneNumber)
                .chooseShippingMethod(shippingPrice)
                .clickOnNextButton()
                .assertOrderTotal(subtotal, shippingPrice)
                .assertShippingDetails(firstName, lastName, address, city, zipCode, country)
                .assertShippingMethod(shippingMethod)
                .clickOnPlaceOrderButton()
                .assertOrderConfirmationMessage(checkoutData.getJsonData("messages.orderConfirmationMessage"));
    }

    @AfterClass
    public void closeBrowser() {
        DriverManager.quit();
    }
}
