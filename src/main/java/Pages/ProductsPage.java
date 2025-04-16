package Pages;

import Utils.ElementActions;
import Utils.LogsUtil;
import Utils.Scrolling;
import Utils.Waits;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static Utils.SoftAssertion.getSoftAssert;

public class ProductsPage {

    //Variables
    private final WebDriver driver;

    //Locators
    private final By categoryTitle = By.cssSelector("#page-title-heading > span");
    private final By mainCategory = By.cssSelector("ul.items > li:nth-of-type(2) > a");
    private final By subCategory = By.cssSelector("ul.items > li:nth-of-type(3) > a");
    private final By targetCategory = By.cssSelector("ul.items > li:nth-of-type(4) > strong");
    private final By firstProduct = By.xpath("//div[contains(@class,'products')]//li[1]/div/a");
    private final By styleSection = By.xpath("//div[text()[contains(., 'Style')]]/parent::div");
    private final By sizeSection = By.xpath("//div[text()[contains(., 'Size')]]/parent::div");
    private final By priceSection = By.xpath("//div[text()[contains(., 'Price')]]/parent::div");
    private final By sorter = By.xpath("//div[contains(@class,'toolbar-products')][1]//div[contains(@class,'toolbar-sorter')]/select");
    private final By sortOrderChanger = By.xpath("//div[contains(@class,'main')]//div[contains(@class,'toolbar-products')][1]//a[@data-role='direction-switcher']");
    private final By searchResultTitles = By.xpath("//strong[contains(@class,'product-item-name')]/a");
    private final By noResultsMessage = By.xpath("//div[contains(@class,'notice')]/div");

    //Constructor
    public ProductsPage(WebDriver driver) {
        this.driver = driver;
    }

    //Actions
    @Step("Selecting the first product")
    public ProductPage selectFirstProduct() {
        ElementActions.clickElement(driver, firstProduct);
        LogsUtil.info("Selected the first product");
        return new ProductPage(driver);
    }

    @Step("Select style: {style}")
    public ProductsPage selectStyle(String style) {
        ElementActions.clickElement(driver, styleSection);
        By styleLocator = By.xpath("//div[text()[contains(., 'Style')]]/parent::div//li/a[text()[contains(., '" + style + "')]]");
        ElementActions.clickElement(driver, styleLocator);
        LogsUtil.info("Selected style is: " + style);
        return this;
    }

    @Step("Select size: {size}")
    public ProductsPage selectSize(String size) {
        ElementActions.clickElement(driver, sizeSection);
        By sizeLocator = By.xpath("//div[text()[contains(., 'Size')]]/parent::div//a[@aria-label='" + size + "']//div");
        ElementActions.clickElement(driver, sizeLocator);
        LogsUtil.info("Selected size is: " + size);
        return this;
    }

    @Step("Select price range: {price}")
    public ProductsPage selectPriceRange(String lowPrice, String highPrice) {
        ElementActions.clickElement(driver, priceSection);
        By priceRangeLocator = By.xpath("//div[text()[contains(., 'Price')]]/parent::div//ol//span[text()[contains(.," + lowPrice + ")]]//parent::a");
        ElementActions.clickElement(driver, priceRangeLocator);
        LogsUtil.info("Selected price range is: " + lowPrice + " - " + highPrice);
        return this;
    }

    @Step("Select sorting products by price")
    public ProductsPage selectSortByPrice() {
        ElementActions.selectFromDropdown(driver, sorter, "Price");
        LogsUtil.info("Selected sorting by price option");
        return this;
    }

    @Step("Change the sorting order")
    public ProductsPage changeSortingOrder() {
        ElementActions.clickElement(driver, sortOrderChanger);
        LogsUtil.info("Sorting order has been changed");
        return this;
    }

    @Step("Add the product: {productName} to the wishlist")
    public WishListPage addProductToWishlist(String productName) {
        By product = By.xpath("//a[contains(text(),'" + productName + "')]");
        ElementActions.hoverOnElement(driver, product);
        By wishlistButton = By.xpath("//a[contains(text(),'" + productName + "')]//parent::strong//parent::div//a[contains(@class,'towishlist')]");
        ElementActions.clickElement(driver, wishlistButton);
        LogsUtil.info("Added the product: " + productName + " to the wishlist");
        return new WishListPage(driver);
    }

    @Step("View the product: {productName}")
    public ProductPage viewProduct(String productName) {
        By product = By.xpath("//ol[contains(@class,'product-items')]//a[contains(text(),'" + productName + "')]");
        ElementActions.clickElement(driver, product);
        LogsUtil.info("Viewed the product: " + productName);
        return new ProductPage(driver);
    }

    @Step("Add the product to the cart")
    public ProductPage addProductToCart() {
        ElementActions.hoverOnElement(driver, firstProduct);
        By addToCartButton = By.xpath("//button[@title='Add to Cart']");
        driver.findElement(firstProduct).findElement(addToCartButton).click();
        LogsUtil.info("Clicked on the element: ", addToCartButton.toString());
        return new ProductPage(driver);
    }

    //Assertions
    @Step("Asert that the category: {TargetCategory} is selected")
    public ProductsPage assertCategorySelected(String MainCategory, String SubCategory, String TargetCategory) {
        //Assert the breadcrumbs categories
        String mainCategoryText = ElementActions.getText(driver, mainCategory);
        String subCategoryText = ElementActions.getText(driver, subCategory);
        String targetCategoryText = ElementActions.getText(driver, targetCategory);
        getSoftAssert().assertEquals(mainCategoryText, MainCategory, "Main Category is not as expected");
        getSoftAssert().assertEquals(subCategoryText, SubCategory, "Sub Category is not as expected");
        getSoftAssert().assertEquals(targetCategoryText, TargetCategory, "Target Category is not as expected");
        //Assert the category title
        String categoryTitleText = ElementActions.getText(driver, categoryTitle);
        getSoftAssert().assertEquals(categoryTitleText, TargetCategory, "Category Title is not as expected");
        return this;
    }

    @Step("Assert that products are sorted in ascending order by price")
    public ProductsPage assertProductsSortedAscendingByPrice() {
        String firstProductPrice = ElementActions.getText(driver, By.xpath("//div[contains(@class,'products')]//li[1]//span[@class='price']"));
        String secondProductPrice = ElementActions.getText(driver, By.xpath("//div[contains(@class,'products')]//li[2]//span[@class='price']"));
        //Assert that the first product price is less than or equal to the second product price
        boolean result = Double.parseDouble(firstProductPrice.replace("$", "")) <= Double.parseDouble(secondProductPrice.replace("$", ""));
        getSoftAssert().assertTrue(result, "Products are not sorted in ascending order by price.");
        return this;
    }

    @Step("Assert that products are sorted in descending order by price")
    public ProductsPage assertProductsSortedDescendingByPrice() {
        String firstProductPrice = ElementActions.getText(driver, By.xpath("//div[contains(@class,'products')]//li[1]//span[@class='price']"));
        String secondProductPrice = ElementActions.getText(driver, By.xpath("//div[contains(@class,'products')]//li[2]//span[@class='price']"));
        //Assert that the first product price is less than or equal to the second product price
        boolean result = Double.parseDouble(firstProductPrice.replace("$", "")) >= Double.parseDouble(secondProductPrice.replace("$", ""));
        getSoftAssert().assertTrue(result, "Products are not sorted in descending order by price.");
        return this;
    }

    @Step("Assert that the search results match: {searchTerm}")
    public ProductsPage assertSearchResultsAreCorrect(String searchTerm) {
        Waits.waitForElementVisible(driver, searchResultTitles);
        Scrolling.scrollToElement(driver, searchResultTitles);
        List<WebElement> productsTitles = ElementActions.findElements(driver, searchResultTitles);
        boolean matchingResult = productsTitles.stream().allMatch(title -> title.getText().toLowerCase().contains(searchTerm.toLowerCase()));
        getSoftAssert().assertTrue(matchingResult, "Not all product titles contain the search term: " + searchTerm);
        return this;
    }

    @Step("Assert that no results found message is displayed")
    public ProductsPage assertNoResultsFound(String expectedMessage) {
        String actualMessage = ElementActions.getText(driver, noResultsMessage);
        getSoftAssert().assertEquals(actualMessage, expectedMessage, "No results found message is not displayed");
        return this;
    }
}
