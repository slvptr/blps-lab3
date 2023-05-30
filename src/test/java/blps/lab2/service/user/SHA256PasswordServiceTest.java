package blps.lab2.service.user;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class SHA256PasswordServiceTest {

    @Test
    void testHashCode() throws NoSuchAlgorithmException {
        SHA256PasswordService passwordService = new SHA256PasswordService();

        String hash = passwordService.hashCode("test@test.test.");
        System.out.println(hash);

        assertEquals(1, 1);
    }
}