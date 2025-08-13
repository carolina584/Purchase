package co.purchase.qa.tasks;

import co.purchase.qa.exceptions.ExceptionProduct;
import co.purchase.qa.usersinterfaces.DetailProductPage;
import co.purchase.qa.usersinterfaces.HomePage;
import co.purchase.qa.usersinterfaces.ListProductPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.thucydides.core.annotations.Step;
import org.openqa.selenium.Keys;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class SearchForProduct implements Task {

    private final String productName;

    public SearchForProduct(String productName) {
        this.productName = productName;
    }

    @Step("{0} searches for the product called")
    @Override
    public <T extends Actor> void performAs(T actor) {

        try {
        actor.attemptsTo(
                Enter.theValue(productName).into(HomePage.INPUT_SEARCH).thenHit(Keys.ENTER),
                Click.on(ListProductPage.CHOOSE_PRODUCT),
                Enter.theValue("2").into(DetailProductPage.INPUT_ADD_PRODUCT));
    }catch (Exception e) {
            throw new ExceptionProduct(ExceptionProduct.ELEMENT_MESSAGE, e);
        }}
    public static SearchForProduct named(String productName) {
        return instrumented(SearchForProduct.class, productName);
    }
}