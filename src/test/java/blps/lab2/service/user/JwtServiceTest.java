package blps.lab2.service.user;

import blps.lab2.model.domain.user.User;
import blps.lab2.model.domain.user.UserRole;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    JwtService jwtService;
    User user;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        String email = "test@test.test";
        UserRole role = UserRole.USER;

        user = new User(email, "test", role, 100, new Date());
    }

    @Test
    void generateTokens() throws JOSEException, ParseException {
        Long expiresIn = 123123L;
        TokensPair tokens = jwtService.generateTokens(user, expiresIn, expiresIn);

        SignedJWT signedJWT = SignedJWT.parse(tokens.getAccessToken());
        JWSVerifier verifier = new MACVerifier("123");
        assertTrue(signedJWT.verify(verifier));

        signedJWT = SignedJWT.parse(tokens.getRefreshToken());
        assertTrue(signedJWT.verify(verifier));
    }

    @Test
    void verifyAndParseClaims() throws JOSEException {
        long expiresIn = 60;
        TokensPair tokens = jwtService.generateTokens(user, expiresIn, expiresIn);

        Map<String, Object> m = jwtService.verifyAndParseClaims(tokens.getAccessToken());
        assertEquals(m.get("role"), "USER");
    }
}