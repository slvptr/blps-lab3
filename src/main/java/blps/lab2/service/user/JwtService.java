package blps.lab2.service.user;

import blps.lab2.controller.exceptions.AuthenticationFailException;
import blps.lab2.model.domain.user.User;
import blps.lab2.model.domain.user.UserRole;
import blps.lab2.utils.DateUtils;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.util.Pair;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret}")
    private String secret;


    public TokensPair generateTokens(User user, Long accessExpiresIn, Long refreshExpiresIn) throws JOSEException {
        JWSSigner signer = new MACSigner(secret);

        long currentUTCSeconds = DateUtils.currentUTCSeconds();
        JWTClaimsSet accessTokenClaimsSet = new JWTClaimsSet.Builder()
                .claim("userId", user.getId())
                .claim("role", user.getRole().name())
                .claim("iat", currentUTCSeconds)
                .claim("exp", currentUTCSeconds + accessExpiresIn)
                .build();
        SignedJWT accessTokenJWS = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), accessTokenClaimsSet);
        accessTokenJWS.sign(signer);

        JWTClaimsSet refreshTokenClaimsSet = new JWTClaimsSet.Builder()
                .claim("userId", user.getId())
                .claim("iat", currentUTCSeconds)
                .claim("exp", currentUTCSeconds + refreshExpiresIn)
                .build();
        SignedJWT refreshTokenJWS = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), refreshTokenClaimsSet);
        refreshTokenJWS.sign(signer);

        return new TokensPair(accessTokenJWS.serialize(), refreshTokenJWS.serialize());
    }

    public Map<String, Object> verifyAndParseClaims(String token) throws JOSEException {
        JWSVerifier verifier = new MACVerifier(secret);
        SignedJWT signedJWT;
        try {
            signedJWT = SignedJWT.parse(token);
        } catch (ParseException e) {
            throw new AuthenticationFailException(e.getMessage());
        }

        if (!signedJWT.verify(verifier)) {
            throw new AuthenticationFailException("Signature is not verified");
        }

        JWTClaimsSet claimsSet;
        try {
            claimsSet = signedJWT.getJWTClaimsSet();
            long userId = Long.parseLong(claimsSet.getClaim("userId").toString());
            if (userId < 0) {
                throw new AuthenticationFailException();
            }
            long exp = ((Date) claimsSet.getClaim("exp")).getTime() / 1000;
            if (DateUtils.currentUTCSeconds() >= exp) {
                throw new AuthenticationFailException("Token has expired");
            }
        } catch (NumberFormatException e) {
            throw new AuthenticationFailException("Failed to parse expiration date");
        } catch (ParseException e) {
            throw new AuthenticationFailException(e.getMessage());
        }

        return claimsSet.getClaims();
    }

}
