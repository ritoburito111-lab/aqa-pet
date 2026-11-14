package ui.tests;

import org.junit.jupiter.api.Test;
import ui.pages.LoginPage;

import static com.codeborne.selenide.Condition.text;

public class LoggingTest extends BaseTest {

    private static final String WRONG_PASS_ERROR_MESSAGE = "Username and password do not match any user in this service";
    private static final String LOCKED_OUT_USER_ERROR_MESSAGE = "Sorry, this user has been locked out";
    private static final String EMPTY_FIELDS_ERROR_MESSAGE = "Username is required";

    @Test
    public void successfulLoginTest() {
        new LoginPage()
                .openLoginPage()
                .fillUsername(USER_NAME_STANDARD)
                .fillPassword(USER_PASSWORD)
                .clickLogin()
                .shouldBeOpened();
    }

    @Test
    public void loginWithWrongPasswordShowsError() {
        new LoginPage()
                .openLoginPage()
                .fillUsername(USER_NAME_STANDARD)
                .fillPassword(USER_PASSWORD_WRONG)
                .clickLoginWithError()
                .errorMessage()
                .shouldHave(text(WRONG_PASS_ERROR_MESSAGE));
    }

    @Test
    public void loginLockedOutUserShowsError() {
        new LoginPage()
                .openLoginPage()
                .fillUsername(USER_NAME_LOCKED)
                .fillPassword(USER_PASSWORD)
                .clickLoginWithError()
                .errorMessage()
                .shouldHave(text(LOCKED_OUT_USER_ERROR_MESSAGE));
    }

    @Test
    public void loginEmptyFieldsShowsError() {
        new LoginPage()
                .openLoginPage()
                .clickLoginWithError()
                .errorMessage()
                .shouldHave(text(EMPTY_FIELDS_ERROR_MESSAGE));
    }
}
