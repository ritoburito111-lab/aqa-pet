package ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Condition.text;

public class ProductsPage {

    private final SelenideElement title = $(".title");
    private final SelenideElement firstItemAddButton = $$(".btn_inventory").first();
    private final ElementsCollection addToCartButtons = $$(".btn_inventory");

    private final Header header = new Header();

    @Step("Проверка перехода на страницу")
    public ProductsPage shouldBeOpened() {
        title.shouldHave(text("Products"));
        return this;
    }

    @Step("Добавить первый элемент в корзину")
    public ProductsPage addFirstItemToCart() {
        firstItemAddButton.click();
        return this;
    }

    @Step("Проверка наличия товаров в корзине")
    public ProductsPage shouldHaveItemsInCart(int count) {
        header.shouldHaveItemsInCart(count);
        return this;
    }

    @Step("Добавить несколько товаров в корзину по индексам: {indexes}")
    public List<String> addItemsToCartAndGetNames(int... indexes) {
        List<String> names = new ArrayList<>();
        for (int index : indexes) {
            SelenideElement item = $$(".inventory_item").get(index);
            names.add(
                    $$(".inventory_item").get(index)
                            .$(".inventory_item_name").getText());
            addToCartButtons.get(index).click();
        }
        return names;
    }

    @Step("Открыть корзину")
    public CartPage openCart() {
        header.openCart();
        return new CartPage();
    }

}
