package co.purchase.qa.usersinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class DetailProductPage {

    public static final Target INPUT_ADD_PRODUCT = Target.the("Input Add Product").located(By.xpath("//input[@id='input-quantity']"));

    public static final Target ADD_TO_CART = Target.the("Add To Cart").located(By.xpath("//button[@id='button-cart']"));

    public static final Target BTN_ITEMS_CART = Target.the("Btn Items Cart").located(By.xpath("//div[@id='cart']"));
    public static final Target BTN_VIEW_CART = Target.the("Btn View Cart").located(By.xpath("//*[@id='cart']/ul/li[2]/div/p/a[1]"));
}
