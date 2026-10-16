package co.purchase.qa.tasks;

import co.purchase.qa.utils.Constants;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;
import net.thucydides.model.util.EnvironmentVariables;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class GoTo implements Task {

    private EnvironmentVariables environmentVariables;

    public GoTo() {}

    @Override
    @Step("{0} goes to the home page")
    public <T extends Actor> void performAs(T actor) {
        String url = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty(Constants.ENV_DEFAULT);
        actor.attemptsTo(Open.url(url));
    }

    public static GoTo home() {
        return instrumented(GoTo.class);
    }
}
