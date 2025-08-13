package co.purchase.qa.tasks;

import co.purchase.qa.exceptions.ExceptionProduct;
import co.purchase.qa.usersinterfaces.CheckoutPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.annotations.Step;
import org.openqa.selenium.Keys;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class CompleteCheckout implements Task {

    @Step("{0} complete checkout")
    @Override
    public <T extends Actor> void performAs(T actor) {
        try {
        actor.attemptsTo(
                Click.on(CheckoutPage.GUEST_CHECKOUT),
                Click.on(CheckoutPage.BTN_CONTINUE),
                Enter.theValue("First Name").into(CheckoutPage.INPUT_FIRST_NAME).thenHit(Keys.TAB),
                Enter.theValue("Last Name").into(CheckoutPage.INPUT_LAST_NAME).thenHit(Keys.TAB),
                Enter.theValue("yinaa56@hotmail.com").into(CheckoutPage.INPUT_E_MAIL).thenHit(Keys.TAB),
                Enter.theValue("Telephone").into(CheckoutPage.INPUT_TELEPHONE).thenHit(Keys.TAB),
                Enter.theValue("Company").into(CheckoutPage.INPUT_COMPANY).thenHit(Keys.TAB),
                Enter.theValue("Address1").into(CheckoutPage.INPUT_ADDRESS1).thenHit(Keys.TAB),
                Enter.theValue("Address2").into(CheckoutPage.INPUT_ADDRESS2).thenHit(Keys.TAB),
                Enter.theValue("Medellin").into(CheckoutPage.INPUT_CITY).thenHit(Keys.TAB),
                Enter.theValue("Post Code").into(CheckoutPage.INPUT_POST_CODE).thenHit(Keys.TAB),
                SelectFromOptions.byIndex(1).from(CheckoutPage.COUNTRY_LIST),
                SelectFromOptions.byIndex(1).from(CheckoutPage.REGION_LIST),
                Click.on(CheckoutPage.BTN_CONTINUEE),
                WaitUntil.the(CheckoutPage.TERMS_AGREEMENT_CHECKBOX, isVisible()).forNoMoreThan(10).seconds(),
                Click.on(CheckoutPage.TERMS_AGREEMENT_CHECKBOX),
                Click.on(CheckoutPage.BTN_CONTINUE_),
                Click.on(CheckoutPage.BTN_CONFIRM_ORDER));
    } catch (Exception e) {
            throw new ExceptionProduct(ExceptionProduct.ELEMENT_MESSAGE, e);
        }}

    public static CompleteCheckout asAGuest() {
        return instrumented(CompleteCheckout.class, new CompleteCheckout());
    }
}
