package co.purchase.qa.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/purchase_product.feature",
        glue = "co.purchase.qa.stepsdefinitions",
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class PurchaseProduct {
}
