package Utils;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class BrowserActions {

    //Get the current url
    @Step("Getting the current url")
    public static String getCurrentURL(WebDriver driver) {
        LogsUtil.info("Current url: ", driver.getCurrentUrl());
        return driver.getCurrentUrl();
    }

    //Get the text of alert
    @Step("Getting the alert text")
    public static String getAlertText(WebDriver driver) {
        Waits.waitForAlert(driver);
        LogsUtil.info("Getting the alert text: ", driver.switchTo().alert().getText());
        return driver.switchTo().alert().getText();
    }

    //Accept alert
    @Step("Accepting the alert")
    public static void acceptAlert(WebDriver driver) {
        Waits.waitForAlert(driver);
        driver.switchTo().alert().accept();
        LogsUtil.info("Accepting the alert");
    }

    //Reject alert
    @Step("Rejecting the alert")
    public static void rejectAlert(WebDriver driver) {
        Waits.waitForAlert(driver);
        driver.switchTo().alert().dismiss();
        LogsUtil.info("Rejecting the alert");
    }

    //Get page title
    @Step("Getting the page title")
    public static String getPageTitle(WebDriver driver) {
        LogsUtil.info("Getting the page title: ", driver.getTitle());
        return driver.getTitle();
    }

    //Refresh the page
    @Step("Refreshing the page")
    public static void refreshPage(WebDriver driver) {
        LogsUtil.info("Refreshing the page");
        driver.navigate().refresh();
    }
}
