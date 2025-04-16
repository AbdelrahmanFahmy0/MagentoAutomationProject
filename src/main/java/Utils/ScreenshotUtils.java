package Utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import Drivers.DriverManager;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;

public class ScreenshotUtils {

    public static String SCREENSHOTS_PATH = "test-outputs/Screenshots/";

    //Capturing a screenshot of the current browser window and saves then adding it to allure report
    public static void takeScreenshot(String screenshotName) {
        try {
            File screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
            File screenshotFile = new File(SCREENSHOTS_PATH + screenshotName + ".png");
            FileUtils.copyFile(screenshot, screenshotFile);
            AllureUtils.attachScreenshotToAllure(screenshotName, screenshotFile.getPath());
        } catch (Exception e) {
            LogsUtil.error("Failed to take screenshot: " + e.getMessage());
        }
    }
}
