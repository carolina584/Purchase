package co.purchase.qa.tasks;

import co.purchase.qa.exceptions.ExceptionProduct;
import co.purchase.qa.interactions.ScrollElement;
import co.purchase.qa.usersinterfaces.DetailProductPage;
import co.purchase.qa.usersinterfaces.ShoppingCartPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.DoubleClick;
import net.thucydides.core.annotations.Step;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class AddProductToCart implements Task {

    @Step("Add Product To Cart {0}")
    @Override
    public <T extends Actor> void performAs(T actor) {

        try {
            actor.attemptsTo(
                    Click.on(DetailProductPage.ADD_TO_CART),
                    Click.on(DetailProductPage.BTN_ITEMS_CART),
                    Click.on(DetailProductPage.BTN_VIEW_CART),
                    ScrollElement.to(ShoppingCartPage.LIST_USE_COUPON_CODE),
                    DoubleClick.on(ShoppingCartPage.BTN_CHECKOUT));
        }catch (Exception e) {
            throw new ExceptionProduct(ExceptionProduct.ELEMENT_MESSAGE, e);
        }}
    public static AddProductToCart inPage() {
        return instrumented(AddProductToCart.class, new AddProductToCart());
    }
}
