package co.purchase.qa.usersinterfaces;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;

@DefaultUrl("http://opencart.abstracta.us/index.php?route=common/home")
public class HomePage extends PageObject {

    public static final Target INPUT_SEARCH = Target.the("Input search").located(By.xpath("//*[@id='search']/input"));
    public static final Target BTN_SEARCH = Target.the("Btn search").located(By.xpath("//*[@id='search']/span"));
}
