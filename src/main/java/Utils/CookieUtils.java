package Utils;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.Set;

public class CookieUtils {

    //Retrieving all cookies from the WebDriver instance.
    public static Set<Cookie> getCookies(WebDriver driver) {
        return driver.manage().getCookies();
    }

    //Adding cookies to the specified WebDriver instance.
    public static void addCookies(WebDriver driver, Set<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            driver.manage().addCookie(cookie);
        }
    }
}
