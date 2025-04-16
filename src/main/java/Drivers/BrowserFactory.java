package Drivers;

import Utils.PropertiesUtils;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Map;

public class BrowserFactory {

    //Getting the driver of the passed browser
    public static WebDriver getBrowser(String browser) {
        switch (browser.toLowerCase()) {
            case "edge":
                return new EdgeDriver(getEdgeOptions());
            case "firefox":
                return new FirefoxDriver(getFirefoxOptions());
            default:
                return new ChromeDriver(getChromeOptions());
        }
    }

    //Getting the Chrome browser options
    public static ChromeOptions getChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--disable-infobars");
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.addArguments("--remote-allow-origins=*");
        Map<String, Object> prefs = Map.of(
                "profile.default_content_setting_values.notifications", false,
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "autofill.profile_enabled", false
        );
        chromeOptions.setExperimentalOption("prefs", prefs);
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        if (!PropertiesUtils.getPropertyValue("executionType").equalsIgnoreCase("local")) {
            chromeOptions.addArguments("--headless");
        }
        return chromeOptions;
    }

    //Getting the Edge browser options
    public static EdgeOptions getEdgeOptions() {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.addArguments("--start-maximized");
        edgeOptions.addArguments("--disable-extensions");
        edgeOptions.addArguments("--disable-infobars");
        edgeOptions.addArguments("--disable-notifications");
        edgeOptions.addArguments("--remote-allow-origins=*");
        Map<String, Object> edgePrefs = Map.of(
                "profile.default_content_setting_values.notifications", false,
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "autofill.profile_enabled", false
        );
        edgeOptions.setExperimentalOption("prefs", edgePrefs);
        edgeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        if (!PropertiesUtils.getPropertyValue("executionType").equalsIgnoreCase("local")) {
            edgeOptions.addArguments("--headless");
        }
        return edgeOptions;
    }

    //Getting the Firefox browser options
    public static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--start-maximized");
        firefoxOptions.addArguments("--disable-extensions");
        firefoxOptions.addArguments("--disable-infobars");
        firefoxOptions.addArguments("--disable-notifications");
        firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        firefoxOptions.setAcceptInsecureCerts(true);
        if (!PropertiesUtils.getPropertyValue("executionType").equalsIgnoreCase("local")) {
            firefoxOptions.addArguments("--headless");
        }
        return firefoxOptions;
    }
}
