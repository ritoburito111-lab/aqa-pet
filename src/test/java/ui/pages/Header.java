package ui.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class Header {

    private final SelenideElement cartBadge = $(".shopping_cart_badge");
    private final SelenideElement cartLink = $(".shopping_cart_link");

    public Header shouldHaveItemsInCart(int count) {
        cartBadge.shouldHave(text(String.valueOf(count)));
        return this;
    }

    public void openCart() {
        cartLink.click();
    }
}
