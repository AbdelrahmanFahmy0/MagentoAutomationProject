package Pages;

import Utils.*;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    //Variables
    private final WebDriver driver;

    //Locators
    private final By createAccountLink = By.xpath("//div[contains(@class,'panel')]//li[3]/a");
    private final By loginLink = By.xpath("//div[contains(@class,'panel')]//li[2]/a");
    private final By nameInHeader = By.xpath("//div[contains(@class,'panel')]//ul/li[@class='greet welcome']/span");
    private final By searchBar = By.id("search");
    private final By menuIcon = By.xpath("//div[contains(@class,'header')]//ul[contains(@class,'header')]//button[@data-action='customer-menu-toggle']");
    private final By myWishListLink = By.xpath("//header[@class='page-header']//div[@data-target='dropdown']//li[2]");
    private final By logoutLink = By.xpath("//header[@class='page-header']//div[@data-target='dropdown']//li[3]/a");
    private final By cartIcon = By.xpath("//div[@class='minicart-wrapper']/a");
    private final By viewCartIcon = By.xpath("//div[@id = 'minicart-content-wrapper']//div[@class = 'actions'][2]//a");
    private final By emptyCartMessage = By.cssSelector("div.block-content > strong");
    private final By cartCounter = By.xpath("//a[contains(@class,'showcart')]/span[2]");
    private final By logoutMessage = By.cssSelector(".page-title > .base");

    //Constructor
    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    //Actions
    @Step("Navigate to register page")
    public RegisterPage NavigateToRegisterPage() {
        ElementActions.clickElement(driver, createAccountLink);
        LogsUtil.info("Navigation to register page");
        return new RegisterPage(driver);
    }

    @Step("Navigate to login page")
    public LoginPage NavigateToLoginPage() {
        ElementActions.clickElement(driver, loginLink);
        LogsUtil.info("Navigation to login page");
        return new LoginPage(driver);
    }

    @Step("Navigate to my wishlist page")
    public WishListPage navigateToMyWishListPage() {
        ElementActions.clickElement(driver, menuIcon);
        ElementActions.clickElement(driver, myWishListLink);
        LogsUtil.info("Navigation to my wishlist page");
        return new WishListPage(driver);
    }

    @Step("Navigate to cart page")
    public CartPage navigateToCartPage() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(6))
                    .until(driver1 -> driver1.findElement(cartCounter).isDisplayed());
        } catch (Exception e) {
            LogsUtil.info("Cart is empty");
        }
        ElementActions.clickElement(driver, cartIcon);
        if (!driver.findElement(emptyCartMessage).isDisplayed()) {
            ElementActions.clickElement(driver, viewCartIcon);
        }
        LogsUtil.info("Navigation to cart page");
        return new CartPage(driver);
    }

    @Step("Click on logout link")
    public HomePage clickOnLogoutLink() {
        ElementActions.clickElement(driver, menuIcon);
        ElementActions.clickElement(driver, logoutLink);
        LogsUtil.info("Clicked on logout link");
        return this;
    }

    @Step("Select category: {mainCategory} -> {subCategory} -> {targetCategory}")
    public ProductsPage selectCategory(String mainCategory, String subCategory, String targetCategory) {
        By mainCategoryLocator = By.xpath("//span[text()='" + mainCategory + "']//ancestor::li");
        By subCategoryLocator = By.xpath("//span[text()='" + mainCategory + "']//ancestor::li//li//span[text()='" + subCategory + "']//parent::a//parent::li");
        By targetCategoryLocator = By.xpath("//span[text()='" + mainCategory + "']//ancestor::li//li//span[text()='" + subCategory + "']//parent::a//parent::li//ul//span[text()='" + targetCategory + "']//parent::a");
        ElementActions.hoverOnElement(driver, mainCategoryLocator);
        ElementActions.hoverOnElement(driver, subCategoryLocator);
        Waits.waitForElementClickable(driver, targetCategoryLocator);
        ElementActions.findElement(driver, targetCategoryLocator).click();
        LogsUtil.info("Selected category is: " + targetCategory);
        return new ProductsPage(driver);
    }

    @Step("Search for a product: {searchTerm}")
    public ProductsPage searchForProduct(String searchTerm) {
        ElementActions.sendData(driver, searchBar, searchTerm);
        ElementActions.clickOnKeyChar(driver, searchBar, Keys.ENTER);
        LogsUtil.info("Searched for product: " + searchTerm);
        return new ProductsPage(driver);
    }

    //Assertions
    @Step("Assert the page url")
    public HomePage assertHomePageUrl(String expectedUrl) {
        String currentUrl = BrowserActions.getCurrentURL(driver);
        Validations.validateEquals(currentUrl, expectedUrl, "URL is not as expected");
        return this;
    }

    @Step("Assert the name in header")
    public HomePage assertNameInHeader(String expectedName) {
        Waits.waitForElementValue(driver, nameInHeader);
        String actualName = ElementActions.getText(driver, nameInHeader).replaceAll("^Welcome,\\s*|!$", "");
        Validations.validateEquals(actualName, expectedName, "Name in header is not as expected");
        return this;
    }

    @Step("Assert logout confirmation message")
    public HomePage assertLogoutMessage(String expectedMessage) {
        String actualMessage = ElementActions.getText(driver, logoutMessage);
        Validations.validateEquals(actualMessage, expectedMessage, "Logout message is not as expected");
        return this;
    }
}
