package ui.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {

    private final SelenideElement usernameInput = $("#user-name");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement loginButton = $("#login-button");

    private static final String ERROR_MESSAGE = "[data-test='error']";

    @Step("Открыть страницу авторизации")
    public LoginPage openLoginPage() {
        open("/");
        return this;
    }

    @Step("Ввести имя пользователя: {username}")
    public LoginPage fillUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Step("Ввести пароль: {password}")
    public LoginPage fillPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Нажать Login (успешный вход)")
    public ProductsPage clickLogin() {
        loginButton.click();
        return new ProductsPage();
    }

    @Step("Нажать Login (ожидаем ошибку)")
    public LoginPage clickLoginWithError() {
        loginButton.click();
        return this;
    }

    public SelenideElement errorMessage() {
        return $(ERROR_MESSAGE);
    }

}
