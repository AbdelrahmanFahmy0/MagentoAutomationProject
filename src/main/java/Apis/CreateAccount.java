package Apis;

import Utils.LogsUtil;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static io.restassured.RestAssured.given;

public class CreateAccount {

    public static void createAccount(String firstName, String lastName, String email, String password) {
        //GET the registration page
        Response getResponse = given()
                .when()
                .get("https://magento.softwaretestingboard.com/customer/account/create/");
        //Extract PHPSESSID from cookies
        String sessionId = getResponse.getCookie("PHPSESSID");
        //Extract form_key from HTML using Jsoup
        String responseBody = getResponse.getBody().asString();
        Document doc = Jsoup.parse(responseBody);
        String formKey = doc.select("input[name=form_key]").val();
        //POST to register a new account
        Response postResponse = given()
                .contentType("multipart/form-data")
                .cookie("PHPSESSID", sessionId)
                .multiPart("form_key", formKey)
                .multiPart("firstname", firstName)
                .multiPart("lastname", lastName)
                .multiPart("email", email) // unique email
                .multiPart("password", password)
                .multiPart("password_confirmation", password)
                .when()
                .post("https://magento.softwaretestingboard.com/customer/account/createpost/");
        if (postResponse.getStatusCode() == 302) {
            LogsUtil.info("Account created successfully using create account api");
        } else {
            LogsUtil.error("Account creation failed using create account api");
        }
    }
}
