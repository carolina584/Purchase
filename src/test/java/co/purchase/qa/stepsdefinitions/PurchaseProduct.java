package co.purchase.qa.stepsdefinitions;

import co.purchase.qa.exceptions.ExceptionProduct;
import co.purchase.qa.questions.TheConfirmationMessage;
import co.purchase.qa.tasks.AddProductToCart;
import co.purchase.qa.tasks.CompleteCheckout;
import co.purchase.qa.tasks.SearchForProduct;
import co.purchase.qa.utils.Constants;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.actors.OnStage;
import net.thucydides.model.util.EnvironmentVariables;
import org.hamcrest.Matchers;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;

public class PurchaseProduct {

    private EnvironmentVariables environmentVariables;

    @Given("user enters the application")
    public void userEntersTheApplication() {
        String url = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty(Constants.ENV_DEFAULT);
        OnStage.theActorInTheSpotlight().wasAbleTo(Open.url(url));
    }

    @When("^user searches (.*) and adds product to cart$")
    public void userSearchesAndAddsProductToCart(String productName) {
        OnStage.theActorInTheSpotlight().attemptsTo(
                SearchForProduct.named(productName),
                AddProductToCart.inPage()
        );
    }

    @And("complete checkout as a guest")
    public void completeCheckoutAsAGuest() {
        OnStage.theActorInTheSpotlight().attemptsTo(
                CompleteCheckout.asAGuest()
        );
    }

    @Then("^It shows that your order has been placed (.*)$")
    public void itShowsThatYourOrderHasBeenPlaced(String expectedMessage) {
        OnStage.theActorInTheSpotlight().should(
                seeThat(TheConfirmationMessage.value(), Matchers.equalTo(expectedMessage))
                        .orComplainWith(ExceptionProduct.class, Constants.MSG_ERROR_ORDER)
        );
    }
}
