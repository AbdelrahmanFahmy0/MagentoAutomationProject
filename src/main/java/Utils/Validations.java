package Utils;

import org.testng.Assert;

public class Validations {
    //Assert true
    public static void validateTrue(Boolean condition, String message) {
        Assert.assertTrue(condition, message);
    }

    //Assert false
    public static void validateFalse(Boolean condition, String message) {
        Assert.assertFalse(condition, message);
    }

    //Assert that the actual equals the expected
    public static void validateEquals(String actual, String expected, String message) {
        Assert.assertEquals(actual, expected, message);
    }

    //Assert that the actual doesn't equal the expected
    public static void validateNotEquals(String actual, String expected, String message) {
        Assert.assertNotEquals(actual, expected, message);
    }
}
