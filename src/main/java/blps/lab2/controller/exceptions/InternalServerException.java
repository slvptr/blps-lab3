package blps.lab2.controller.exceptions;

public class InternalServerException extends RuntimeException {
    public InternalServerException(){}
    public InternalServerException(String message) {
        super(message);
    }
}
