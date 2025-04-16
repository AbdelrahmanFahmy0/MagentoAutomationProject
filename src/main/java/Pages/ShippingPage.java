package Pages;

import Utils.ElementActions;
import Utils.LogsUtil;
import Utils.Waits;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static Utils.SoftAssertion.getSoftAssert;

public class ShippingPage {

    //Variables
    private final WebDriver driver;

    //Locators
    private final By email = By.cssSelector("#customer-email-fieldset #customer-email");
    private final By firstName = By.cssSelector("#shipping-new-address-form input[name='firstname']");
    private final By lastName = By.cssSelector("#shipping-new-address-form input[name='lastname']");
    private final By street = By.cssSelector("div[name='shippingAddress.street.0']  input");
    private final By city = By.cssSelector("div[name='shippingAddress.city']  input");
    private final By zipCode = By.cssSelector("div[name='shippingAddress.postcode']  input");
    private final By state = By.cssSelector("div[name='shippingAddress.region_id']  select");
    private final By country = By.cssSelector("div[name='shippingAddress.country_id']  select");
    private final By phoneNumber = By.cssSelector("div[name='shippingAddress.telephone']  input");
    private final By nextButton = By.cssSelector("#shipping-method-buttons-container button");
    private final By newAddressButton = By.cssSelector(".new-address-popup > button");
    private final By shipHereButton = By.xpath("//button[contains(@class,'action-save-address')]");
    private final By emailError = By.id("customer-email-error");
    private final By firstNameError = By.cssSelector("#shipping-new-address-form div.field-error > span");
    private final By lastNameError = By.cssSelector("div[name = 'shippingAddress.lastname'] div.field-error > span");
    private final By streetError = By.cssSelector("div[name = 'shippingAddress.street.0'] div.field-error > span");
    private final By cityError = By.cssSelector("div[name = 'shippingAddress.city'] div.field-error > span");
    private final By stateError = By.cssSelector("div[name = 'shippingAddress.region_id'] div.field-error > span");
    private final By countryError = By.cssSelector("div[name = 'shippingAddress.country_id'] div.field-error > span");
    private final By zipCodeError = By.cssSelector("div[name = 'shippingAddress.postcode'] div.field-error > span");
    private final By phoneNumberError = By.cssSelector("div[name = 'shippingAddress.telephone'] div.field-error > span");
    private final By shippingMethodError = By.cssSelector("#co-shipping-method-form div[role = 'alert'] span");
    private final By blocker = By.xpath("//div[contains(@class,'shipping-address-item')]");

    //Constructor
    public ShippingPage(WebDriver driver) {
        this.driver = driver;
    }

    //Actions
    @Step("Clear first name field")
    public ShippingPage clearFirstName() {
        ElementActions.clearData(driver, firstName);
        return this;
    }

    @Step("Clear last name field")
    public ShippingPage clearLastName() {
        ElementActions.clearData(driver, lastName);
        return this;
    }

    @Step("Enter email: {emailText}")
    public ShippingPage enterEmail(String emailText) {
        ElementActions.sendData(driver, email, emailText);
        return this;
    }

    @Step("Enter first name: {firstNameText}")
    public ShippingPage enterFirstName(String firstNameText) {
        ElementActions.sendData(driver, firstName, firstNameText);
        return this;
    }

    @Step("Enter last name: {lastNameText}")
    public ShippingPage enterLastName(String lastNameText) {
        ElementActions.sendData(driver, lastName, lastNameText);
        return this;
    }

    @Step("Enter street: {streetText}")
    public ShippingPage enterStreet(String streetText) {
        ElementActions.sendData(driver, street, streetText);
        return this;
    }

    @Step("Enter city: {cityText}")
    public ShippingPage enterCity(String cityText) {
        ElementActions.sendData(driver, city, cityText);
        return this;
    }

    @Step("Select state: {stateText}")
    public ShippingPage selectState(String stateText) {
        ElementActions.selectFromDropdown(driver, state, stateText);
        return this;
    }

    @Step("Enter zip code: {zipCodeText}")
    public ShippingPage enterZipCode(String zipCodeText) {
        ElementActions.sendData(driver, zipCode, zipCodeText);
        return this;
    }

    @Step("Select country: {countryText}")
    public ShippingPage selectCountry(String countryText) {
        ElementActions.selectFromDropdown(driver, country, countryText);
        return this;
    }

    @Step("Enter phone number: {phoneNumberText}")
    public ShippingPage enterPhoneNumber(String phoneNumberText) {
        ElementActions.sendData(driver, phoneNumber, phoneNumberText);
        return this;
    }

    @Step("Fill the shipping form")
    public ShippingPage fillShippingForm(String firstNameText, String lastNameText, String streetText, String cityText, String zipCodeText, String countryText, String phoneNumberText) {
        enterFirstName(firstNameText);
        enterLastName(lastNameText);
        enterStreet(streetText);
        enterCity(cityText);
        enterZipCode(zipCodeText);
        selectCountry(countryText);
        enterPhoneNumber(phoneNumberText);
        return this;
    }

    @Step("Choose shipping method")
    public ShippingPage chooseShippingMethod(String shippingPrice) {
        By shippingMethod = By.xpath("//tbody//span[contains(text(),'" + shippingPrice + "')]//ancestor::td//parent::tr//input");
        ElementActions.clickElement(driver, shippingMethod);
        LogsUtil.info("The shipping method with price: " + shippingPrice + " is selected");
        return this;
    }

    @Step("Click on the next button")
    public ReviewPage clickOnNextButton() {
        ElementActions.clickElement(driver, nextButton);
        return new ReviewPage(driver);
    }

    private String getErrorMessage(By locator) {
        return ElementActions.getText(driver, locator);
    }

    @Step("Click on new address button")
    public ShippingPage clickOnNewAddressButton() {
        Waits.waitForElementInvisible(driver, blocker, 2L);
        ElementActions.clickElement(driver, newAddressButton);
        return this;
    }

    @Step("Click on ship here button")
    public ShippingPage clickOnShipHereButton() {
        ElementActions.clickElement(driver, shipHereButton);
        return this;
    }

    //Assertions
    @Step("Assert email error message")
    public ShippingPage assertEmailErrorMessage(String expectedErrorMessage) {
        getSoftAssert().assertEquals(getErrorMessage(emailError), expectedErrorMessage, "The email error message is incorrect.");
        return this;
    }

    @Step("Assert first name error message")
    public ShippingPage assertFirstNameErrorMessage(String expectedErrorMessage) {
        getSoftAssert().assertEquals(getErrorMessage(firstNameError), expectedErrorMessage, "The first name error message is incorrect.");
        return this;
    }

    @Step("Assert last name error message")
    public ShippingPage assertLastNameErrorMessage(String expectedErrorMessage) {
        getSoftAssert().assertEquals(getErrorMessage(lastNameError), expectedErrorMessage, "The last name error message is incorrect.");
        return this;
    }

    @Step("Assert street error message")
    public ShippingPage assertStreetErrorMessage(String expectedErrorMessage) {
        getSoftAssert().assertEquals(getErrorMessage(streetError), expectedErrorMessage, "The street error message is incorrect.");
        return this;
    }

    @Step("Assert city error message")
    public ShippingPage assertCityErrorMessage(String expectedErrorMessage) {
        getSoftAssert().assertEquals(getErrorMessage(cityError), expectedErrorMessage, "The city error message is incorrect.");
        return this;
    }

    @Step("Assert state error message")
    public ShippingPage assertStateErrorMessage(String expectedErrorMessage) {
        getSoftAssert().assertEquals(getErrorMessage(stateError), expectedErrorMessage, "The state error message is incorrect.");
        return this;
    }

    @Step("Assert country error message")
    public ShippingPage assertCountryErrorMessage(String expectedErrorMessage) {
        getSoftAssert().assertEquals(getErrorMessage(countryError), expectedErrorMessage, "The country error message is incorrect.");
        return this;
    }

    @Step("Assert zip code error message")
    public ShippingPage assertZipCodeErrorMessage(String expectedErrorMessage) {
        getSoftAssert().assertEquals(getErrorMessage(zipCodeError), expectedErrorMessage, "The zip code error message is incorrect.");
        return this;
    }

    @Step("Assert phone number error message")
    public ShippingPage assertPhoneNumberErrorMessage(String expectedErrorMessage) {
        getSoftAssert().assertEquals(getErrorMessage(phoneNumberError), expectedErrorMessage, "The phone number error message is incorrect.");
        return this;
    }

    @Step("Assert shipping method error message")
    public ShippingPage assertShippingMethodErrorMessage(String expectedErrorMessage) {
        getSoftAssert().assertEquals(getErrorMessage(shippingMethodError), expectedErrorMessage, "The shipping method error message is incorrect.");
        return this;
    }
}
