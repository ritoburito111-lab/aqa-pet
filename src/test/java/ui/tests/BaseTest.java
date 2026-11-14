package ui.tests;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ui.utils.TestData;

public class BaseTest {
    protected static final String USER_NAME_STANDARD = TestData.get("user.name.standard");
    protected static final String USER_PASSWORD = TestData.get("user.password");
    protected static final String USER_PASSWORD_WRONG = TestData.get("user.password.wrong");
    protected static final String USER_NAME_LOCKED = TestData.get("user.name.locked");

    @BeforeEach
    void setupAllure() {
        SelenideLogger.addListener("allure", new AllureSelenide()
                .screenshots(true)
                .includeSelenideSteps(true)
                .savePageSource(true));
    }

    @AfterEach
    void tearDown() {
        Selenide.closeWebDriver();
    }
}
