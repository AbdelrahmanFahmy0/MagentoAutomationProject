package Utils;

import io.qameta.allure.Allure;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class AllureUtils {

    //This method gets the latest log file and attach it to the allure report as string.
    public static void attachLogsToAllureReport() {
        try {
            File logFile = FileUtils.getLatestFile(LogsUtil.LOGS_PATH);
            if (!logFile.exists()) {
                LogsUtil.warn("Log file does not exist: " + LogsUtil.LOGS_PATH);
                return;
            }
            Allure.addAttachment("logs.log", Files.readString(Path.of(logFile.getPath())));
            LogsUtil.info("Logs attached to allure report");
        } catch (Exception e) {
            LogsUtil.error("Failed to attach logs to Allure report: " + e.getMessage());
        }
    }

    //This method attaches the taken screenshot to the allure report.
    public static void attachScreenshotToAllure(String screenshotName, String screenshotPath) {
        try {
            Allure.addAttachment(screenshotName, Files.newInputStream(Path.of(screenshotPath)));
        } catch (Exception e) {
            LogsUtil.error("Failed to attach screenshot to Allure report: " + e.getMessage());
        }
    }
}
