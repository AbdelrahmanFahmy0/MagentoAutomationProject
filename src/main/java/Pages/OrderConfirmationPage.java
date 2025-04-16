package Pages;

import Utils.ElementActions;
import Utils.Waits;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static Utils.SoftAssertion.getSoftAssert;

public class OrderConfirmationPage {

    //Variables
    private final WebDriver driver;

    //Locators
    private final By ConfirmationMessage = By.className("base");
    private final By OrderNumber = By.className("checkout-success");

    //Constructor
    public OrderConfirmationPage(WebDriver driver) {
        this.driver = driver;
    }

    //Actions
    @Step("Getting order confirmation message")
    private String getConfirmationMessage() {
        Waits.waitForElementVisible(driver, OrderNumber);
        return ElementActions.getText(driver, ConfirmationMessage);
    }

    //Assertions
    @Step("Assert order confirmation message")
    public OrderConfirmationPage assertOrderConfirmationMessage(String expectedMessage) {
        getSoftAssert().assertEquals(getConfirmationMessage(), expectedMessage, "Order confirmation message is not as expected");
        return this;
    }
}
