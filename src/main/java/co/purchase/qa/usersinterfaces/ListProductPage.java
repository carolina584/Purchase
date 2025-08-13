package co.purchase.qa.usersinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class ListProductPage {
    public static final Target CHOOSE_PRODUCT = Target.the("Choose product").located(By.xpath("//*[@id='content']/div[3]/div[1]/div/div[1]/a/img"));
}
