package ui.tests;

import org.junit.jupiter.api.Test;
import ui.pages.LoginPage;
import ui.pages.ProductsPage;

import java.util.List;


public class CartTest extends BaseTest{

    @Test
    public void addFirstElementToCart() {
        new LoginPage()
                .openLoginPage()
                .fillUsername(USER_NAME_STANDARD)
                .fillPassword(USER_PASSWORD)
                .clickLogin()
                .shouldBeOpened()
                .addFirstItemToCart()
                .shouldHaveItemsInCart(1);
    }

    @Test
    public void addedItemsShouldBeInCart() {
        ProductsPage productsPage = new LoginPage()
                .openLoginPage()
                .fillUsername(USER_NAME_STANDARD)
                .fillPassword(USER_PASSWORD)
                .clickLogin()
                .shouldBeOpened();

        List<String> addedNames = productsPage.addItemsToCartAndGetNames(1, 3, 5);

        productsPage.openCart()
                .shouldHaveItemsCount(addedNames.size())
                .shouldContainItems(addedNames);
    }
}
