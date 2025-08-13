package co.purchase.qa.stepsdefinitions;

import co.purchase.qa.questions.TheConfirmationMessage;
import co.purchase.qa.tasks.AddProductToCart;
import co.purchase.qa.tasks.CompleteCheckout;
import co.purchase.qa.tasks.GoTo;
import co.purchase.qa.tasks.SearchForProduct;
import co.purchase.qa.usersinterfaces.HomePage;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.thucydides.core.annotations.Managed;
import org.openqa.selenium.WebDriver;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class PurchaseProduct {

    @Managed(driver= "chrome")
    private WebDriver hisBrowser;

    private Actor user = Actor.named("Carolina");
    private HomePage page;

    @Before
    public void setUp() {
        user.can(BrowseTheWeb.with(hisBrowser));
    }

    @Given("user enters the application")
    public void userEntersTheApplication() {
        user.wasAbleTo(GoTo.home());}

    @When("^user searches (.*) and adds product to cart")
    public void userSearchesMacBookAndAddsProductToCart(String productName) {
        user.attemptsTo(SearchForProduct.named(productName),
                AddProductToCart.inPage());}
    @And("complete checkout as a guest")
    public void completeCheckoutAsAGuest() {
        user.attemptsTo(CompleteCheckout.asAGuest());
    }

    @Then("It shows that your order has been placed (.*)")
    public void itShowsThatYourOrderHasBeenPlaced(String expectedMessage) {
        user.should(seeThat(TheConfirmationMessage.value(), equalTo(expectedMessage)));
    }
}
