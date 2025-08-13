package co.purchase.qa.usersinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class ShoppingCartPage {

    public static final Target LIST_USE_COUPON_CODE  = Target.the("List User Coupon Code").located(By.xpath("//div[@id='accordion']/div[1]"));
    public static final Target BTN_CHECKOUT = Target.the("Btn checkout").located(By.xpath("//*[@id='content']/div[3]/div[2]/a"));
}
