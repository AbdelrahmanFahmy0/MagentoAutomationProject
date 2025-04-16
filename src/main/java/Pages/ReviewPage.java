package Pages;

import Utils.ElementActions;
import Utils.Waits;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.text.DecimalFormat;

import static Utils.SoftAssertion.getSoftAssert;

public class ReviewPage {

    //Variables
    private final WebDriver driver;

    //Locators
    private final By orderTotal = By.xpath("//td[contains(@data-th,'Total')]//span");
    private final By shippingMethod = By.cssSelector(".ship-via .shipping-information-content span");
    private final By shipping = By.cssSelector(".ship-to .shipping-information-content");
    private final By placeOrderButton = By.xpath("//div[@class='payment-method-content']/div[@class='actions-toolbar']//button");
    private final By blocker = By.xpath("//div[contains(@class,'billing-address-same-as-shipping-block')]");

    //Constructor
    public ReviewPage(WebDriver driver) {
        this.driver = driver;
    }

    //Actions
    private String calculateOrderTotal(String subtotal, String shipping) {
        double subtotalValue = Double.parseDouble(subtotal);
        double shippingValue = Double.parseDouble(shipping);
        double orderTotal = subtotalValue + shippingValue;
        DecimalFormat df = new DecimalFormat("0.00");
        return String.valueOf(df.format(orderTotal));
    }

    @Step("Getting order total value")
    private String getOrderTotal() {
        return ElementActions.getText(driver, orderTotal).replace("$", "");
    }

    @Step("Getting shipping method value")
    private String getShippingMethod() {
        return ElementActions.getText(driver, shippingMethod);
    }

    @Step("Getting shipping details")
    private String getShippingDetails() {
        return ElementActions.getText(driver, shipping);
    }

    @Step("Click on place order button")
    public OrderConfirmationPage clickOnPlaceOrderButton() {
        Waits.waitForElementInvisible(driver, blocker, 2L);
        ElementActions.clickElement(driver, placeOrderButton);
        return new OrderConfirmationPage(driver);
    }

    //Assertions
    @Step("Assert order total")
    public ReviewPage assertOrderTotal(String subtotal, String shipping) {
        String expectedOrderTotal = calculateOrderTotal(subtotal, shipping);
        getSoftAssert().assertEquals(getOrderTotal(), expectedOrderTotal, "Order total is not correct");
        return this;
    }

    @Step("Assert shipping method")
    public ReviewPage assertShippingMethod(String expectedShippingMethod) {
        getSoftAssert().assertEquals(getShippingMethod(), expectedShippingMethod, "Shipping method is not correct");
        return this;
    }

    @Step("Assert shipping details")
    public ReviewPage assertShippingDetails(String firstName, String lastName, String address, String city, String zipCode, String country) {
        String shippingDetails = getShippingDetails();
        boolean result = shippingDetails.contains(firstName) &&
                shippingDetails.contains(lastName) &&
                shippingDetails.contains(address) &&
                shippingDetails.contains(city) &&
                shippingDetails.contains(zipCode) &&
                shippingDetails.contains(country);
        getSoftAssert().assertTrue(result, "Shipping details are not correct");
        return this;
    }
}
