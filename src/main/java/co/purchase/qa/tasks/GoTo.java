package co.purchase.qa.tasks;

import net.serenitybdd.screenplay.Actor;
import co.purchase.qa.usersinterfaces.HomePage;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;
import net.thucydides.core.annotations.Step;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class GoTo implements Task {
    public GoTo() {}
    @Override
    @Step("{0} goes to the home page")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Open.browserOn().the(HomePage.class));
    }

    public static GoTo home() {
        return instrumented(GoTo.class);}
}