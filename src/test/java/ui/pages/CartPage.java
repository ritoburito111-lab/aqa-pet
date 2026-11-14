package ui.pages;

import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;

import java.util.List;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selenide.$$;

public class CartPage {
    private final ElementsCollection itemNames = $$(".cart_item .inventory_item_name");

    @Step("Проверка количества товаров: {count} шт")
    public CartPage shouldHaveItemsCount(int count) {
        itemNames.shouldHave(size(count));
        return this;
    }

    @Step("Проверить состав корзины")
    public CartPage shouldContainItems(List<String> expectedNames) {
        itemNames.shouldHave(exactTexts(expectedNames));
        return this;
    }
}
