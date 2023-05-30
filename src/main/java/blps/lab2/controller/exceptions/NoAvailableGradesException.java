package blps.lab2.controller.exceptions;

public class NoAvailableGradesException extends RuntimeException {
    public NoAvailableGradesException(){}
    public NoAvailableGradesException(String message) {
        super(message);
    }
}
