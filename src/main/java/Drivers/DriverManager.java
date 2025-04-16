package Drivers;

import Utils.LogsUtil;
import Utils.PropertiesUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static org.testng.AssertJUnit.fail;

public class DriverManager {

    //Defining the thread local
    public static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    //Initializing the WebDriver
    @Step("Open the browser")
    public static void setupDriver() {
        String browser = PropertiesUtils.getPropertyValue("Browser");
        WebDriver driver = BrowserFactory.getBrowser(browser);
        LogsUtil.info("Driver created on: ", browser);
        driverThreadLocal.set(driver);
    }

    //Getting the current driver
    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            LogsUtil.error("Driver is null");
            fail("Driver is null");
        }
        return driverThreadLocal.get();
    }

    //Closing and removing the driver
    public static void quit() {
        getDriver().quit();
        driverThreadLocal.remove();
        LogsUtil.info("Closing the browser");
    }
}
