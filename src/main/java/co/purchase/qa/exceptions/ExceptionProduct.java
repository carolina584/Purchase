package co.purchase.qa.exceptions;

import net.serenitybdd.core.exceptions.SerenityManagedException;

public class ExceptionProduct  extends SerenityManagedException {

    private static final long serialVersionUID = 1L;
    public static final String ELEMENT_MESSAGE = "Element not found";

    public ExceptionProduct(String message, Throwable testErrorException) {
        super(message, testErrorException);
    }
}
