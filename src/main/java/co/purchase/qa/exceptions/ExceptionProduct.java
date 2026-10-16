package co.purchase.qa.exceptions;

public class ExceptionProduct extends Error {

    public static final String ELEMENT_MESSAGE = "El elemento no fue encontrado en la página";

    public ExceptionProduct(String message, Throwable cause) {
        super(message, cause);
    }
}
