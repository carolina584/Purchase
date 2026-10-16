package co.purchase.qa.usersinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class HomePage {

    public static final Target INPUT_SEARCH = Target.the("Input search").located(By.xpath("//*[@id='search']/input"));
    public static final Target BTN_SEARCH = Target.the("Btn search").located(By.xpath("//*[@id='search']/span"));

    private HomePage() {
        throw new IllegalStateException("Utility class");
    }
}
