package Pages;

import Utils.BrowserActions;
import Utils.ElementActions;
import Utils.Validations;
import Utils.Waits;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MyAccountPage {

    //Variables
    private final WebDriver driver;

    //Locators
    private final By confirmationMessage = By.xpath("//div[@class='messages']/div/div");
    private final By nameInHeader = By.xpath("//div[contains(@class,'panel')]//ul/li[@class='greet welcome']/span");

    //Constructor
    public MyAccountPage(WebDriver driver) {
        this.driver = driver;
    }

    //Actions

    //Assertions
    @Step("Assert the page url")
    public MyAccountPage assertMyAccountPageUrl(String expectedUrl) {
        String currentUrl = BrowserActions.getCurrentURL(driver);
        Validations.validateEquals(currentUrl, expectedUrl, "URL is not as expected");
        return this;
    }

    @Step("Assert the registration confirmation message")
    public MyAccountPage assertConfirmationMessage(String expectedMessage) {
        String actualMessage = ElementActions.getText(driver, confirmationMessage);
        Validations.validateEquals(actualMessage, expectedMessage, "Confirmation message is not as expected");
        return this;
    }

    @Step("Assert the name in header")
    public MyAccountPage assertNameInHeader(String expectedName) {
        Waits.waitForElementValue(driver, nameInHeader);
        String actualName = ElementActions.getText(driver, nameInHeader).replaceAll("^Welcome,\\s*|!$", "");
        ;
        Validations.validateEquals(actualName, expectedName, "Name in header is not as expected");
        return this;
    }
}
