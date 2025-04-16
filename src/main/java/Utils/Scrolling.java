package Utils;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class Scrolling {

    //Scrolling to element
    @Step("Scroll to element {locator}")
    public static void scrollToElement(WebDriver driver, By locator) {
        LogsUtil.info("Scrolling to element: ", locator.toString());
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView();", ElementActions.findElement(driver, locator));
    }
}
