package co.purchase.qa.utils;

public class Constants {

    // Ambientes (claves para EnvironmentSpecificConfiguration)
    public static final String ENV_DEFAULT = "environments.default.webdriver.base.url";
    public static final String ENV_QA      = "environments.qa.webdriver.base.url";
    public static final String ENV_DEV     = "environments.dev.webdriver.base.url";

    // Mensajes de error (usados en orComplainWith)
    public static final String MSG_ERROR_LOGIN    = "No se pudo verificar el login exitoso";
    public static final String MSG_ERROR_PRODUCTO = "El producto no fue encontrado en la página";
    public static final String MSG_ERROR_ORDER    = "No se pudo verificar la orden de compra";

    // Timeouts
    public static final int WAIT_SHORT  = 5;
    public static final int WAIT_MEDIUM = 10;
    public static final int WAIT_LONG   = 30;

    private Constants() {
        throw new IllegalStateException("Utility class");
    }
}
