package Utils;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class ElementActions {

    //Send data to element
    @Step("Sending data: {data} to element: {locator}")
    public static void sendData(WebDriver driver, By locator, String data) {
        Waits.waitForElementVisible(driver, locator);
        Scrolling.scrollToElement(driver, locator);
        findElement(driver, locator).sendKeys(data);
        LogsUtil.info("Data entered: ", data, " in the field: ", locator.toString());
    }

    @Step("Clearing data from element: {locator}")
    public static void clearData(WebDriver driver, By locator) {
        Waits.waitForElementVisible(driver, locator);
        Scrolling.scrollToElement(driver, locator);
        findElement(driver, locator).clear();
        LogsUtil.info("Cleared data from the field: ", locator.toString());
    }

    //Click on element
    @Step("Clicking on element: {locator}")
    public static void clickElement(WebDriver driver, By locator) {
        Waits.waitForElementClickable(driver, locator);
        Scrolling.scrollToElement(driver, locator);
        findElement(driver, locator).click();
        LogsUtil.info("Clicked on the element: ", locator.toString());
    }

    //Get the text of element
    @Step("Getting the text from element: {locator}")
    public static String getText(WebDriver driver, By locator) {
        Waits.waitForElementVisible(driver, locator);
        Scrolling.scrollToElement(driver, locator);
        LogsUtil.info("Getting text from the element: ", locator.toString(), " Text is ", findElement(driver, locator).getText());
        return findElement(driver, locator).getText();
    }

    //Hover over element
    @Step("Hovering over element: {locator}")
    public static void hoverOnElement(WebDriver driver, By locator) {
        Waits.waitForElementVisible(driver, locator);
        Scrolling.scrollToElement(driver, locator);
        new Actions(driver).moveToElement(findElement(driver, locator)).perform();
        LogsUtil.info("Hovered over the element: ", locator.toString());
    }

    //Select element from dropdown
    @Step("Selecting element from dropdown: {locator}")
    public static void selectFromDropdown(WebDriver driver, By locator, String text) {
        Waits.waitForElementVisible(driver, locator);
        Scrolling.scrollToElement(driver, locator);
        new Select(findElement(driver, locator)).selectByVisibleText(text);
        LogsUtil.info("Selected from dropdown: ", text);
    }

    //Click on key char
    @Step("Clicking on key char")
    public static void clickOnKeyChar(WebDriver driver, By locator, Keys keys) {
        new Actions(driver)
                .sendKeys(findElement(driver, locator), keys)
                .perform();
        LogsUtil.info("Clicked on key char: ", keys.toString());
    }

    //Waiting and scrolling to element
    public static void waitAndScroll(WebDriver driver, By locator) {
        Waits.waitForElementVisible(driver, locator);
        Scrolling.scrollToElement(driver, locator);
    }

    //Finding element
    public static WebElement findElement(WebDriver driver, By locator) {
        return driver.findElement(locator);
    }

    //Finding elements
    public static List<WebElement> findElements(WebDriver driver, By locator) {
        return driver.findElements(locator);
    }
}
