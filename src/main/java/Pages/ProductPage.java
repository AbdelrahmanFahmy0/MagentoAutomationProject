package Pages;

import Utils.ElementActions;
import Utils.LogsUtil;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static Utils.SoftAssertion.getSoftAssert;

public class ProductPage {

    //Variables
    private final WebDriver driver;

    //Locators
    private final By productName = By.xpath("//span[@itemprop='name']");
    private final By categoryOfProduct = By.cssSelector("ul.items > li:nth-of-type(4) > a");
    private final By moreInformation = By.id("tab-label-additional-title");
    private final By styleOfProduct = By.xpath("//th[text() = 'Style']//parent::tr/td");
    private final By sizesOfProduct = By.xpath("//div[contains(@aria-labelledby,'size')]/div");
    private final By priceOfProduct = By.cssSelector("div.product-info-price .price-wrapper > span");
    private final By alertMessage = By.xpath("//div[@role='alert']/div/div");
    private final By quantityField = By.id("qty");
    private final By addToCartButton = By.id("product-addtocart-button");
    private final By cartCounter = By.className("counter-number");
    private final By cartIcon = By.xpath("//div[@class='minicart-wrapper']/a");
    private final By viewCartIcon = By.xpath("//div[@id = 'minicart-content-wrapper']//div[@class = 'actions'][2]//a");
    private final By sizeErrorMessage = By.xpath("//div[@attribute-code='size']/div[@class='mage-error']");
    private final By colorErrorMessage = By.xpath("//div[@attribute-code='color']/div[@class='mage-error']");
    private final By quantityErrorMessage = By.xpath("//div[@class='product-options-bottom']//div[@class='mage-error']");

    //Constructor
    public ProductPage(WebDriver driver) {
        this.driver = driver;
    }

    //Actions
    private String getProductCategory() {
        String productCategoryText = ElementActions.getText(driver, categoryOfProduct);
        LogsUtil.info("The product category is: " + productCategoryText);
        return productCategoryText;
    }

    private String getProductStyle() {
        String productStyleText = ElementActions.getText(driver, styleOfProduct);
        LogsUtil.info("The product style is: " + productStyleText);
        return productStyleText;
    }

    private List<WebElement> getSizeOptions() {
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(driver1 -> {
            WebElement sizeSection = driver1.findElement(sizesOfProduct);
            return sizeSection.isDisplayed();
        });
        return driver.findElements(sizesOfProduct);
    }

    @Step("Click on more information")
    public ProductPage clickOnMoreInformation() {
        ElementActions.clickElement(driver, moreInformation);
        LogsUtil.info("Clicked on more information");
        return this;
    }

    @Step("Get the product name")
    private String getProductName() {
        String productNameText = ElementActions.getText(driver, productName);
        LogsUtil.info("The product name is: " + productNameText);
        return productNameText;
    }

    @Step("Get the product price")
    private String getProductPrice() {
        String productPriceText = ElementActions.getText(driver, priceOfProduct).replace("$", "");
        LogsUtil.info("The product price is: " + productPriceText);
        return productPriceText;
    }

    @Step("Get the cart counter")
    public String getCartCounter() {
        String cartCounterText = ElementActions.getText(driver, cartCounter);
        LogsUtil.info("The cart counter is: " + cartCounterText);
        return cartCounterText;
    }

    @Step("Choose the product size: {size}")
    public ProductPage chooseProductSize(String size) {
        By sizeOption = By.xpath("//div[contains(@aria-labelledby,'size')]/div[text() = '" + size + "']");
        ElementActions.clickElement(driver, sizeOption);
        LogsUtil.info("Choosing the product size: " + size);
        return this;
    }

    @Step("Choose the product color: {color}")
    public ProductPage chooseProductColor(String color) {
        By colorOption = By.xpath("//div[contains(@aria-labelledby,'color')]/div[@aria-label='" + color + "']");
        ElementActions.clickElement(driver, colorOption);
        LogsUtil.info("Choosing the product color: " + color);
        return this;
    }

    @Step("Enter the product quantity: {quantity}")
    public ProductPage enterProductQuantity(String quantity) {
        ElementActions.clearData(driver, quantityField);
        ElementActions.sendData(driver, quantityField, quantity);
        LogsUtil.info("Entering the product quantity: " + quantity);
        return this;
    }

    @Step("Click on add to cart button")
    public ProductPage clickOnAddToCartButton() {
        ElementActions.clickElement(driver, addToCartButton);
        return this;
    }

    @Step("Navigate to cart page")
    public CartPage navigateToCartPage() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5)).until(driver1 -> {
                WebElement counter = driver1.findElement(cartCounter);
                String text = counter.getText().trim();
                return !text.equals("0") && !text.isEmpty();
            });
        } catch (Exception e) {
            LogsUtil.info("Cart is empty or did not update in time");
        }
        ElementActions.clickElement(driver, cartIcon);
        ElementActions.clickElement(driver, viewCartIcon);
        LogsUtil.info("Navigation to cart page");
        return new CartPage(driver);
    }

    //Assertions
    @Step("Assert that the product is categorized under: {TargetCategory}")
    public ProductPage assertProductCategory(String TargetCategory) {
        //Assert the product is categorized under the target category
        getSoftAssert().assertEquals(getProductCategory(), TargetCategory, "The product is not categorized under the " + TargetCategory + " category");
        return this;
    }

    @Step("Assert the style of the product is: {style}")
    public ProductPage assertProductStyle(String style) {
        //Assert the product style
        getSoftAssert().assertTrue(getProductStyle().contains(style), "The product style is not as expected");
        return this;
    }

    @Step("Assert the size options contains: {size}")
    public ProductPage assertProductSize(String size) {
        //Assert the product size
        boolean isSizeAvailable = getSizeOptions().stream().anyMatch(sizeOption -> sizeOption.getText().equalsIgnoreCase(size));
        getSoftAssert().assertTrue(isSizeAvailable, "The product size is not as expected");
        return this;
    }

    @Step("Assert the price of the product is between: {lowPrice} and {highPrice}")
    public ProductPage assertProductPrice(String lowPrice, String highPrice) {
        //Assert the product price
        String productPriceText = ElementActions.getText(driver, priceOfProduct);
        double productPrice = Double.parseDouble(productPriceText.replace("$", ""));
        double lowPriceValue = Double.parseDouble(lowPrice);
        double highPriceValue = Double.parseDouble(highPrice);
        boolean isPriceInRange = productPrice >= lowPriceValue && productPrice <= highPriceValue;
        getSoftAssert().assertTrue(isPriceInRange, "The product price is not within the expected range");
        return this;
    }

    @Step("Assert that no options chosen message is displayed")
    public ProductPage assertNoOptionsMessage(String expectedMessage) {
        String actualMessage = ElementActions.getText(driver, alertMessage);
        getSoftAssert().assertEquals(actualMessage, expectedMessage, "No options chosen message is not displayed");
        return this;
    }

    @Step("Assert the product details")
    public ProductPage assertProductDetails(String expectedName, String expectedPrice) {
        String actualName = getProductName();
        String actualPrice = getProductPrice();
        getSoftAssert().assertEquals(actualName, expectedName, "Product name does not match");
        getSoftAssert().assertEquals(actualPrice, expectedPrice, "Product price does not match");
        return this;
    }

    @Step("Assert that product is added to cart")
    public ProductPage assertProductAddedToCart(String expectedCartCounter) {
        String actualMessage = ElementActions.getText(driver, alertMessage);
        String expectedMessage = "You added " + getProductName();
        boolean isMessageDisplayed = actualMessage.contains(expectedMessage);
        getSoftAssert().assertTrue(isMessageDisplayed, "Confirmation message is not displayed");
        String actualCartCounter = getCartCounter();
        getSoftAssert().assertEquals(actualCartCounter, expectedCartCounter, "Cart counter is not updated");
        return this;
    }

    @Step("Assert that size error message is displayed")
    public ProductPage assertSizeErrorMessage(String expectedMessage) {
        String actualMessage = ElementActions.getText(driver, sizeErrorMessage);
        getSoftAssert().assertEquals(actualMessage, expectedMessage, "Size error message is not displayed");
        return this;
    }

    @Step("Assert that color error message is displayed")
    public ProductPage assertColorErrorMessage(String expectedMessage) {
        String actualMessage = ElementActions.getText(driver, colorErrorMessage);
        getSoftAssert().assertEquals(actualMessage, expectedMessage, "Color error message is not displayed");
        return this;
    }

    @Step("Assert that quantity error message is displayed")
    public ProductPage assertQuantityErrorMessage(String expectedMessage) {
        String actualMessage = ElementActions.getText(driver, quantityErrorMessage);
        getSoftAssert().assertEquals(actualMessage, expectedMessage, "Quantity error message is not displayed");
        return this;
    }
}
