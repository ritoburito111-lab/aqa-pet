package rest;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ApiTest {

    @Test
    @Step("Проверка API reqres.in")
    void checkPublicApiReturnsUsers() {
        given().baseUri("https://reqres.in/api")
                .when().get("users?page=2")
                .then()
                .statusCode(200)
                .body("data", not(empty()))
                .body("data[0].email", containsString("@"));
    }
}
