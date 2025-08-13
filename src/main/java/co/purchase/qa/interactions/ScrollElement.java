package co.purchase.qa.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.actions.Scroll;
import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.Step;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class ScrollElement implements Interaction {

    private final Target target;

    public ScrollElement(Target target) {
        this.target = target;
    }

    @Override
    @Step("{0} scrolls to the element '#target'")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Scroll.to(target)
        );
    }
    public static Interaction to(Target target) {
        return instrumented(ScrollElement.class, target);
    }
}
