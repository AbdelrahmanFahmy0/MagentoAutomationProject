package Tests;

import Drivers.DriverManager;
import Listeners.TestNGListeners;
import Pages.CartPage;
import Pages.HomePage;
import Utils.BrowserActions;
import Utils.CookieUtils;
import Utils.JsonUtils;
import Utils.TimestampUtils;
import com.github.javafaker.Faker;
import io.qameta.allure.*;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.util.Set;

import static Apis.CreateAccount.createAccount;
import static Utils.PropertiesUtils.getPropertyValue;

@Owner("Abdelrahman Fahmy")
@Epic("Website")
@Feature("Checkout")
@Listeners(TestNGListeners.class)
public class CheckoutTest {

    //Variables
    WebDriver driver;
    JsonUtils loginData = new JsonUtils("auth-data");
    JsonUtils filterData = new JsonUtils("filters-data");
    JsonUtils checkoutData = new JsonUtils("checkout-data");
    JsonUtils productData = new JsonUtils("products-data");
    String password = loginData.getJsonData("user.password");
    String productName = productData.getJsonData("products[0].name");
    String firstName = checkoutData.getJsonData("user.firstName");
    String lastName = checkoutData.getJsonData("user.lastName");
    Faker faker = new Faker();
    String address = faker.address().fullAddress();
    String city = faker.address().cityName();
    String zipCode = faker.address().zipCode();
    String country = checkoutData.getJsonData("user.country");
    String phoneNumber = faker.phoneNumber().phoneNumber();
    String shippingPrice = checkoutData.getJsonData("shippingMethod.price");
    String shippingMethod = checkoutData.getJsonData("shippingMethod.method");
    Set<Cookie> cookies;

    //Methods
    @BeforeClass
    public void beforeClass() {
        String email = "Abdelrahman-" + TimestampUtils.getTimestamp() + "@gmail.com";
        createAccount(firstName, lastName, email, password);
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
        new HomePage(driver)
                .selectCategory(filterData.getJsonData("category.mainCategory"),
                        filterData.getJsonData("category.subCategory"),
                        filterData.getJsonData("category.targetCategory"))
                .viewProduct(productName)
                .chooseProductSize(productData.getJsonData("products[0].size"))
                .chooseProductColor(productData.getJsonData("products[0].color"))
                .enterProductQuantity(productData.getJsonData("products[0].quantity"))
                .clickOnAddToCartButton()
                .navigateToCartPage();
    }

    @Test(priority = 1, description = "Valid Order Checkout")
    @Description("Verify that user can checkout with valid data")
    @Severity(SeverityLevel.BLOCKER)
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

    @Test(priority = 2, description = "Checkout with Empty First Name")
    @Description("Verify that user cannot checkout with empty first name")
    @Severity(SeverityLevel.MINOR)
    public void checkoutWithEmptyFirstNameTC() {
        new CartPage(driver)
                .clickOnCheckoutButton()
                .clickOnNewAddressButton()
                .clearFirstName()
                .fillShippingForm("", "", address, city, zipCode, country, phoneNumber)
                .clickOnShipHereButton()
                .assertFirstNameErrorMessage(checkoutData.getJsonData("messages.emptyFieldMessage"));
    }

    @Test(priority = 2, description = "Checkout with Empty Last Name")
    @Description("Verify that user cannot checkout with empty last name")
    @Severity(SeverityLevel.MINOR)
    public void checkoutWithEmptyLastNameTC() {
        new CartPage(driver)
                .clickOnCheckoutButton()
                .clickOnNewAddressButton()
                .clearLastName()
                .fillShippingForm("", "", address, city, zipCode, country, phoneNumber)
                .clickOnShipHereButton()
                .assertLastNameErrorMessage(checkoutData.getJsonData("messages.emptyFieldMessage"));
    }

    @Test(priority = 2, description = "Checkout with Empty Address")
    @Description("Verify that user cannot checkout with empty address")
    @Severity(SeverityLevel.CRITICAL)
    public void checkoutWithEmptyAddressTC() {
        new CartPage(driver)
                .clickOnCheckoutButton()
                .clickOnNewAddressButton()
                .fillShippingForm("", "", "", city, zipCode, country, phoneNumber)
                .clickOnShipHereButton()
                .assertStreetErrorMessage(checkoutData.getJsonData("messages.emptyFieldMessage"));
    }

    @Test(priority = 2, description = "Checkout with Empty City")
    @Description("Verify that user cannot checkout with empty city")
    @Severity(SeverityLevel.CRITICAL)
    public void checkoutWithEmptyCityTC() {
        new CartPage(driver)
                .clickOnCheckoutButton()
                .clickOnNewAddressButton()
                .fillShippingForm("", "", address, "", zipCode, country, phoneNumber)
                .clickOnShipHereButton()
                .assertCityErrorMessage(checkoutData.getJsonData("messages.emptyFieldMessage"));
    }

    @Test(priority = 2, description = "Checkout with Empty Country")
    @Description("Verify that user cannot checkout with empty country")
    @Severity(SeverityLevel.CRITICAL)
    public void checkoutWithEmptyCountryTC() {
        new CartPage(driver)
                .clickOnCheckoutButton()
                .clickOnNewAddressButton()
                .fillShippingForm("", "", address, city, zipCode, "", phoneNumber)
                .clickOnShipHereButton()
                .assertCountryErrorMessage(checkoutData.getJsonData("messages.emptyFieldMessage"));
    }

    @Test(priority = 2, description = "Checkout with Empty Zip Code")
    @Description("Verify that user cannot checkout with empty zip code")
    @Severity(SeverityLevel.NORMAL)
    public void checkoutWithEmptyZipCodeTC() {
        new CartPage(driver)
                .clickOnCheckoutButton()
                .clickOnNewAddressButton()
                .fillShippingForm("", "", address, city, "", country, phoneNumber)
                .clickOnShipHereButton()
                .assertZipCodeErrorMessage(checkoutData.getJsonData("messages.emptyFieldMessage"));
    }

    @Test(priority = 2, description = "Checkout with Empty Phone Number")
    @Description("Verify that user cannot checkout with empty phone number")
    @Severity(SeverityLevel.CRITICAL)
    public void checkoutWithEmptyPhoneNumberTC() {
        new CartPage(driver)
                .clickOnCheckoutButton()
                .clickOnNewAddressButton()
                .fillShippingForm("", "", address, city, zipCode, country, "")
                .clickOnShipHereButton()
                .assertPhoneNumberErrorMessage(checkoutData.getJsonData("messages.emptyFieldMessage"));
    }

    @Test(priority = 2, description = "Checkout with Empty State")
    @Description("Verify that user cannot checkout with empty state")
    @Severity(SeverityLevel.CRITICAL)
    public void checkoutWithEmptyStateTC() {
        new CartPage(driver)
                .clickOnCheckoutButton()
                .clickOnNewAddressButton()
                .fillShippingForm("", "", address, city, zipCode, checkoutData.getJsonData("user.country2"), phoneNumber)
                .clickOnShipHereButton()
                .assertStateErrorMessage(checkoutData.getJsonData("messages.emptyFieldMessage"));
    }

    @AfterMethod
    public void closeBrowser() {
        DriverManager.quit();
    }
}
