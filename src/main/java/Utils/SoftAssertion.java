package Utils;

import org.testng.ITestResult;
import org.testng.asserts.SoftAssert;

public class SoftAssertion {

    // Thread-local variable to hold a separate SoftAssert instance for each thread.
    private static final ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);

    //Retrieves the SoftAssert instance for the current thread.
    public static SoftAssert getSoftAssert() {
        return softAssert.get();
    }

    public static void assertAll(ITestResult testResult) {
        SoftAssert currentSoftAssert = getSoftAssert();
        try {
            // Finalize and check all soft assertions. If any assertion fails, the test fails and an AssertionError is thrown.
            currentSoftAssert.assertAll("Soft Assertion"); // Assert all recorded assertions (if any)
        } catch (AssertionError error) {
            LogsUtil.error("Soft Assertion Failed: {}", error.getMessage());
            testResult.setStatus(ITestResult.FAILURE);
            testResult.setThrowable(error);
        } finally {
            softAssert.remove(); // Always clean up the ThreadLocal instance
        }
    }
}
