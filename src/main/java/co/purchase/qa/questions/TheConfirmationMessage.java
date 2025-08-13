package co.purchase.qa.questions;

import co.purchase.qa.usersinterfaces.CheckoutPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
public class TheConfirmationMessage implements Question<String> {

    @Override
    public String answeredBy(Actor actor) {
        return Text.of(CheckoutPage.TITLE_ORDER).viewedBy(actor).asString();
    }
    public static Question<String> value() {
        return new TheConfirmationMessage();
    }
}
