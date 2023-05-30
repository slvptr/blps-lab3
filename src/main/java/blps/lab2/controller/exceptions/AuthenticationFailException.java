package blps.lab2.controller.exceptions;

public class AuthenticationFailException extends RuntimeException {
    public AuthenticationFailException(){}
    public AuthenticationFailException(String message) {
        super(message);
    }
}
