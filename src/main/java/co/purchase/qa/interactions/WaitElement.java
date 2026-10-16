package co.purchase.qa.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitElement implements Interaction {

    private final Target target;
    private final String state;

    public WaitElement(Target target, String state) {
        this.target = target;
        this.state = state;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        WebDriverWait wait = new WebDriverWait(
                BrowseTheWeb.as(actor).getDriver(), Duration.ofSeconds(10));
        switch (state) {
            case "Visible":
                wait.until(ExpectedConditions.visibilityOf(target.resolveFor(actor)));
                break;
            case "Invisible":
                wait.until(ExpectedConditions.invisibilityOf(target.resolveFor(actor)));
                break;
            case "Enabled":
                wait.until(ExpectedConditions.elementToBeClickable(target.resolveFor(actor)));
                break;
            default:
                throw new IllegalArgumentException("Estado no soportado: " + state);
        }
    }

    public static WaitElement untilAppears(Target target) {
        return Tasks.instrumented(WaitElement.class, target, "Visible");
    }

    public static WaitElement untilDisappears(Target target) {
        return Tasks.instrumented(WaitElement.class, target, "Invisible");
    }

    public static WaitElement untilBeEnabled(Target target) {
        return Tasks.instrumented(WaitElement.class, target, "Enabled");
    }
}
