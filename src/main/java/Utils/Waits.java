package Utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;

public class Waits {

    //wait for element to be present
    public static WebElement waitForElementPresent(WebDriver driver, By locator) {
        LogsUtil.info("Waiting for element to be present: ", locator.toString());
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(driver1 -> driver1.findElement(locator));
    }

    //wait for element to be visible
    public static WebElement waitForElementVisible(WebDriver driver, By locator) {
        LogsUtil.info("Waiting for element to be visible: ", locator.toString());
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(driver1 -> {
                    WebElement element = waitForElementPresent(driver, locator);
                    return element.isDisplayed() ? element : null;
                });
    }

    //wait for element to be clickable
    public static WebElement waitForElementClickable(WebDriver driver, By locator) {
        LogsUtil.info("Waiting for element to be clickable: ", locator.toString());
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(driver1 -> {
                    WebElement element = waitForElementVisible(driver, locator);
                    return element.isEnabled() ? element : null;
                });
    }

    // Wait for element to be invisible
    public static void waitForElementInvisible(WebDriver driver, By locator, Long timeout) {
        LogsUtil.info("Waiting for element to be invisible: ", locator.toString());
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeout))
                    .until(driver1 -> !driver1.findElement(locator).isDisplayed());
        } catch (Exception e) {
            LogsUtil.warn(e.getMessage());
        }
    }

    // Wait for alert to be visible
    public static Alert waitForAlert(WebDriver driver) {
        LogsUtil.info("Waiting for alert to be visible");
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(driver1 -> driver1.switchTo().alert());
        } catch (Exception e) {
            return null;
        }
    }

    // Wait for element to have specific attribute value
    public static void waitForAttribute(WebDriver driver, By locator, String attribute, String value) {
        LogsUtil.info("Waiting for the element ", locator.toString(), " to has attribute ", attribute, " equals ", value);
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(driver1 -> Objects.equals(driver1.findElement(locator).getDomAttribute(attribute), value));
        } catch (Exception e) {
            LogsUtil.error(e.getMessage());
        }
    }

    // Wait for cookie to have value and not being null
    public static void waitForCookie(WebDriver driver, String cookieName) {
        LogsUtil.info("Waiting for the cookie " + cookieName + " to exist");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(driver1 -> driver1.manage().getCookieNamed(cookieName) != null);
        } catch (Exception e) {
            LogsUtil.error(e.getMessage());
        }
    }

    // wait for element to have value
    public static void waitForElementValue(WebDriver driver, By locator) {
        LogsUtil.info("Waiting for the element ", locator.toString(), " to have value");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(driver1 -> !driver1.findElement(locator).getText().isEmpty());
        } catch (Exception e) {
            LogsUtil.error(e.getMessage());
        }
    }

}
