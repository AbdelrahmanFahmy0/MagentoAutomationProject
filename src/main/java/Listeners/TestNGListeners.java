package Listeners;

import Utils.*;
import org.testng.*;

import java.io.File;

public class TestNGListeners implements IExecutionListener, ITestListener, IInvokedMethodListener {

    File allureResults = new File("test-outputs/allure-results");
    File screenshots = new File("test-outputs/Screenshots");
    File logs = new File("test-outputs/Logs");
    File srcEnvironmentFile = new File("src/test/resources/environment.properties");
    File desEnvironmentFile = new File("test-outputs/allure-results/environment.properties");

    @Override
    public void onExecutionStart() {
        LogsUtil.info("Test execution started");
        FileUtils.deleteFiles(allureResults);
        FileUtils.cleanDirectory(screenshots);
        FileUtils.cleanDirectory(logs);
        FileUtils.createDirectory(logs);
        FileUtils.createDirectory(screenshots);
        FileUtils.createDirectory(allureResults);
        FileUtils.copyFile(srcEnvironmentFile, desEnvironmentFile);
        PropertiesUtils.loadProperties();
    }

    @Override
    public void onExecutionFinish() {
        LogsUtil.info("Test execution finished");
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            //Assert all the soft assertions after running each test method.
            SoftAssertion.assertAll(testResult);
            //Taking screenshot and adding it to allure report.
            switch (testResult.getStatus()) {
                case ITestResult.SUCCESS -> ScreenshotUtils.takeScreenshot("passed-" + testResult.getName());
                case ITestResult.FAILURE -> ScreenshotUtils.takeScreenshot("failed-" + testResult.getName());
                case ITestResult.SKIP -> ScreenshotUtils.takeScreenshot("skipped-" + testResult.getName());
            }
            AllureUtils.attachLogsToAllureReport();
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LogsUtil.info("Test case ", result.getName(), " passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LogsUtil.info("Test case ", result.getName(), " failed");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LogsUtil.info("Test case ", result.getName(), " skipped");
    }
}
