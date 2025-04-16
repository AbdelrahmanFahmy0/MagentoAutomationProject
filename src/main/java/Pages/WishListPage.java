package Pages;

import Utils.*;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static Utils.SoftAssertion.getSoftAssert;

public class WishListPage {

    //Variables
    private final WebDriver driver;

    //Locators
    private final By emptyWishListMessage = By.xpath("//div[contains(@class,'empty')]/span");
    private final By addedProductMessage = By.xpath("//div[@role='alert']/div/div");
    private final By latestProduct = By.xpath("//form[@id='wishlist-view-form']//li[last()]");
    private final By productNameLocator = By.xpath("//div[@class='product-item-info']/strong/a");
    private final By addProductToCartButton = By.xpath("//div[@class='product-item-info']/div[@class='product-item-inner']//button");
    private final By removeProductButton = By.xpath("//div[@class='product-item-info']/div[@class='product-item-inner']//a[2]");

    //Constructor
    public WishListPage(WebDriver driver) {
        this.driver = driver;
    }

    //Actions
    private String getLatestProductName() {
        ElementActions.waitAndScroll(driver, latestProduct);
        String actualProductName = driver.findElement(latestProduct).findElement(productNameLocator).getText();
        LogsUtil.info("Getting text from the element: ", productNameLocator.toString(), " Text is ", actualProductName);
        return actualProductName;
    }

    @Step("Remove the product from the wishlist")
    public WishListPage removeProductFromWishList() {
        ElementActions.waitAndScroll(driver, latestProduct);
        ElementActions.hoverOnElement(driver, latestProduct);
        driver.findElement(latestProduct).findElement(removeProductButton).click();
        LogsUtil.info("Clicked on the remove product button");
        return this;
    }

    @Step("Add product to the cart")
    public ProductPage addProductToCart() {
        ElementActions.waitAndScroll(driver, latestProduct);
        ElementActions.hoverOnElement(driver, latestProduct);
        driver.findElement(latestProduct).findElement(addProductToCartButton).click();
        LogsUtil.info("Clicked on the add product to cart button");
        return new ProductPage(driver);
    }

    //Assertions
    @Step("Assert the wishlist page url")
    public WishListPage assertWishListPageUrl(String expectedUrl) {
        String actualUrl = BrowserActions.getCurrentURL(driver);
        getSoftAssert().assertEquals(actualUrl, expectedUrl, "The wishlist page URL is incorrect.");
        return this;
    }

    @Step("Assert that the wishlist is empty")
    public WishListPage assertEmptyWishList(String expectedMessage) {
        String actualMessage = ElementActions.getText(driver, emptyWishListMessage);
        getSoftAssert().assertEquals(actualMessage, expectedMessage, "The wishlist is not empty.");
        return this;
    }

    @Step("Assert that the wishlist contains {productName}")
    public WishListPage assertWishListContainsProduct(String productName) {
        getSoftAssert().assertEquals(getLatestProductName(), productName, "The wishlist does not contain the expected product: " + productName);
        return this;
    }
}
