package blps.lab2.service.user;

import java.security.NoSuchAlgorithmException;

public interface PasswordService {
    String hashCode(String password) throws NoSuchAlgorithmException;
}
