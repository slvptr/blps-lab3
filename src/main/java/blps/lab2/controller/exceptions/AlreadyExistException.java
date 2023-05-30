package blps.lab2.controller.exceptions;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(){}
    public AlreadyExistException(String message) {
        super(message);
    }
}
