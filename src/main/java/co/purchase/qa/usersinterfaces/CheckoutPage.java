package co.purchase.qa.usersinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;
public class CheckoutPage {

    public static final Target GUEST_CHECKOUT = Target.the("Guest Checkout").located(By.xpath("//*[@id='collapse-checkout-option']/div/div/div[1]/div[2]/label/input"));
    public static final Target BTN_CONTINUE = Target.the("Btn Continue").located(By.xpath("//input[@id='button-account']"));
    public static final Target INPUT_FIRST_NAME = Target.the("Input First Name").located(By.xpath("//*[@id='input-payment-firstname']"));
    public static final Target INPUT_LAST_NAME = Target.the("Input Last Name").located(By.xpath("//input[@id='input-payment-lastname']"));
    public static final Target INPUT_E_MAIL = Target.the("Input E MAIL").located(By.xpath("//input[@id='input-payment-email']"));
    public static final Target INPUT_TELEPHONE = Target.the("Input Telephone").located(By.xpath("//input[@id='input-payment-telephone']"));
    public static final Target INPUT_COMPANY = Target.the("Input Company").located(By.xpath("//input[@id='input-payment-company']"));
    public static final Target INPUT_ADDRESS1 = Target.the("Input Address1").located(By.xpath("//input[@id='input-payment-address-1']"));
    public static final Target INPUT_ADDRESS2 = Target.the("Input Address2").located(By.xpath("//input[@id='input-payment-address-2']"));
    public static final Target INPUT_CITY = Target.the("Input City").located(By.xpath("//input[@id='input-payment-city']"));
    public static final Target INPUT_POST_CODE = Target.the("Input Post Code").located(By.xpath("//input[@id='input-payment-postcode']"));
    public static final Target COUNTRY_LIST = Target.the("Country list")
            .located(By.id("input-payment-country"));
    public static final Target REGION_LIST = Target.the("region list")
            .located(By.id("input-payment-zone"));
    public static final Target BTN_CONTINUEE = Target.the("Btn continuee").locatedBy("//input[@id='button-guest']");
    public static final Target TERMS_AGREEMENT_CHECKBOX = Target.the("terms and conditions agreement checkbox")
            .located(By.xpath("(//input[@value='1'])[2]"));
    public static final Target BTN_CONTINUE_ = Target.the("Btn Continue").locatedBy("//input[@id='button-payment-method']");
    //hacer scroll
    public static final Target BTN_CONFIRM_ORDER = Target.the("Btn Confirm Order").locatedBy("//input[@id='button-confirm']");
    public static final Target TITLE_ORDER = Target.the("Title Order").locatedBy("(//*[contains(text(), 'Your order has been placed!')])[2]");
}
