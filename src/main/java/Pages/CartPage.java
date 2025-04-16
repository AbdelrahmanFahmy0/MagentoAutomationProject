package Pages;

import Utils.*;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.List;

import static Utils.SoftAssertion.getSoftAssert;

public class CartPage {

    //Variables
    private final WebDriver driver;

    //Locators
    private final By productsInCart = By.xpath("//table[@id='shopping-cart-table']//tbody");
    private final By productName = By.cssSelector(".product-item-name > a");
    private final By productPrice = By.xpath(".//td[contains(@class,'price')]//span[@class ='price']");
    private final By productSize = By.cssSelector(".item-options > dd:nth-of-type(1)");
    private final By productColor = By.cssSelector(".item-options > dd:nth-of-type(2)");
    private final By productQuantity = By.xpath("//td[contains(@class,'qty')]//input");
    private final By subTotalPrice = By.xpath("//td[contains(@class,'subtotal')]//span[@class ='price']");
    private final By emptyCartMessage = By.cssSelector("div.block-content > strong");
    private final By removeProductButton = By.xpath("//div[@class='actions-toolbar']/a[@title='Remove item']");
    private final By clearedCartMessage = By.cssSelector(".cart-empty > p:nth-of-type(1)");
    private final By orderSubtotal = By.xpath("//tr[contains(@class,'totals')]//span[@data-th='Subtotal']");
    private final By orderTax = By.xpath("//tr[@class='totals-tax']//span");
    private final By orderTotalPrice = By.xpath("//tr[contains(@class,'grand')]//span");
    private final By checkoutButton = By.xpath("//button[@data-role='proceed-to-checkout']");


    //Constructor
    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    //Actions
    @Step("Remove the product: {productName} from the cart")
    public CartPage removeProductFromCart(String productName) {
        ElementActions.waitAndScroll(driver, productsInCart);
        List<WebElement> products = ElementActions.findElements(driver, productsInCart);
        WebElement product = products.stream().filter(x -> x.findElement(this.productName).getText().equals(productName))
                .findFirst().orElse(null);
        assert product != null;
        product.findElement(removeProductButton).click();
        LogsUtil.info("Product: " + productName + " removed from the cart");
        return this;
    }

    private WebElement getProduct(String productName) {
        ElementActions.waitAndScroll(driver, productsInCart);
        List<WebElement> products = ElementActions.findElements(driver, productsInCart);
        WebElement product = products.stream().filter(x -> x.findElement(this.productName).getText().equals(productName))
                .findFirst().orElse(null);
        assert product != null;
        return product;
    }

    private String getProductName(WebElement product) {
        return product.findElement(productName).getText();
    }

    private String getProductPrice(WebElement product) {
        return product.findElement(productPrice).getText().replace("$", "");
    }

    private String getProductSize(WebElement product) {
        return product.findElement(productSize).getText();
    }

    private String getProductColor(WebElement product) {
        return product.findElement(productColor).getText();
    }

    private String getProductQuantity(WebElement product) {
        return product.findElement(productQuantity).getDomAttribute("value");
    }

    private String getProductSubTotalPrice(WebElement product) {
        return product.findElement(subTotalPrice).getText().replace("$", "");
    }

    private String calculateProductSubTotalPrice(String productPrice, String productQuantity) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(Double.parseDouble(productPrice) * Double.parseDouble(productQuantity));
    }

    //Order summary
    public String getOrderSubtotalPrice() {
        return ElementActions.getText(driver, orderSubtotal).replace("$", "");
    }

    private String getOrderTax() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(driver1 -> driver1.findElement(orderTax).isDisplayed());
            Scrolling.scrollToElement(driver, orderTax);
            return ElementActions.getText(driver, orderTax).replace("$", "");
        } catch (Exception e) {
            LogsUtil.info("Tax is not available");
            return "0.00";
        }
    }

    private String getOrderTotalPrice() {
        return ElementActions.getText(driver, orderTotalPrice).replace("$", "");
    }

    private String calculateAllProductsSubtotalPrice() {
        double subtotal = 0;
        List<WebElement> products = ElementActions.findElements(driver, productsInCart);
        for (WebElement product : products) {
            String price = product.findElement(productPrice).getText().replace("$", "");
            String quantity = product.findElement(productQuantity).getDomAttribute("value");
            subtotal += Double.parseDouble(price) * Double.parseDouble(quantity);
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(subtotal);
    }

    private String calculateOrderTotalPrice() {
        double subtotal = Double.parseDouble(getOrderSubtotalPrice());
        double tax = Double.parseDouble(getOrderTax());
        double total = subtotal + tax;
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(total);
    }

    @Step("Click on the checkout button")
    public ShippingPage clickOnCheckoutButton() {
        ElementActions.clickElement(driver, checkoutButton);
        LogsUtil.info("Clicked on the checkout button");
        return new ShippingPage(driver);
    }

    //Assertions
    @Step("Assert the cart page url")
    public CartPage assertCartPageUrl(String expectedUrl) {
        String actualUrl = BrowserActions.getCurrentURL(driver);
        getSoftAssert().assertEquals(actualUrl, expectedUrl, "The cart page URL is incorrect.");
        return this;
    }

    @Step("Assert the empty cart message")
    public CartPage assertEmptyCartMessage(String expectedMessage) {
        String actualMessage = ElementActions.getText(driver, emptyCartMessage);
        Validations.validateEquals(actualMessage, expectedMessage, "Empty cart message is not as expected");
        return this;
    }

    @Step("Assert the details of product in the cart")
    public CartPage assertProductDetailsInCart(String productName, String productPrice, String productSize, String productColor, String productQuantity) {
        //Product
        ElementActions.waitAndScroll(driver, productsInCart);
        WebElement product = getProduct(productName);
        //Assertions
        getSoftAssert().assertEquals(getProductName(product), productName, "The product name in the cart is incorrect.");
        getSoftAssert().assertEquals(getProductPrice(product), productPrice, "The product price in the cart is incorrect.");
        getSoftAssert().assertEquals(getProductSize(product), productSize, "The product size in the cart is incorrect.");
        getSoftAssert().assertEquals(getProductColor(product), productColor, "The product color in the cart is incorrect.");
        getSoftAssert().assertEquals(getProductQuantity(product), productQuantity, "The product quantity in the cart is incorrect.");
        getSoftAssert().assertEquals(getProductSubTotalPrice(product), calculateProductSubTotalPrice(getProductPrice(product), getProductQuantity(product)), "The subtotal price in the cart is incorrect.");
        return this;
    }

    @Step("Assert the order summary")
    public CartPage assertOrderSummary() {
        getSoftAssert().assertEquals(getOrderSubtotalPrice(), calculateAllProductsSubtotalPrice(), "The order subtotal is incorrect.");
        getSoftAssert().assertEquals(getOrderTotalPrice(), calculateOrderTotalPrice(), "The order total is incorrect.");
        return this;
    }

    @Step("Assert the cart is cleared")
    public CartPage assertCartIsCleared(String expectedMessage) {
        String actualMessage = ElementActions.getText(driver, clearedCartMessage);
        Validations.validateEquals(actualMessage, expectedMessage, "The cart is not cleared");
        return this;
    }
}
