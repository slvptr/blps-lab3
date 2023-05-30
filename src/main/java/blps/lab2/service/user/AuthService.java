package blps.lab2.service.user;

import blps.lab2.controller.exceptions.AuthenticationFailException;
import blps.lab2.controller.exceptions.InternalServerException;
import blps.lab2.controller.exceptions.InvalidDataException;
import blps.lab2.model.domain.user.User;
import blps.lab2.model.domain.user.UserRole;
import blps.lab2.model.requests.user.AuthUserRequest;
import blps.lab2.model.requests.user.RefreshUserRequest;
import blps.lab2.model.responses.user.AuthUserResponse;
import blps.lab2.utils.DateUtils;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Service
public class AuthService {
    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordService passwordService;

    @Value("${application.security.jwt.accessExpiresIn}")
    private Long accessExpiresIn;

    @Value("${application.security.jwt.refreshExpiresIn}")
    private Long refreshExpiresIn;

    @Autowired
    public AuthService(JwtService jwtService, UserService userService, PasswordService passwordService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.passwordService = passwordService;
    }

    public User register(AuthUserRequest request) {
        try {
            String password = request.getPassword().trim();
            String hashedPassword = passwordService.hashCode(password);
            User user = new User(
                    request.getEmail().trim().toLowerCase(),
                    hashedPassword,
                    UserRole.USER,
                    100,
                    new Date()
            );
            if (password.length() <= 3) throw new InvalidDataException("Password length must be at least 3");

            return userService.createUser(user);

        } catch (NoSuchAlgorithmException e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public AuthUserResponse login(AuthUserRequest request) {
        try {
            User user = userService.getUserByEmail(request.getEmail())
                    .orElseThrow(() -> new AuthenticationFailException("Such user doesn't exist"));

            if (!user.getPassword().equals(passwordService.hashCode(request.getPassword()))) {
                throw new AuthenticationFailException("Invalid password");
            }

            TokensPair tokens = jwtService.generateTokens(user, accessExpiresIn, refreshExpiresIn);
            return new AuthUserResponse(
                    tokens.getAccessToken(),
                    DateUtils.currentUTCSeconds() + accessExpiresIn,
                    tokens.getRefreshToken()
            );

        } catch (NoSuchAlgorithmException | JOSEException e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public AuthUserResponse refresh(RefreshUserRequest request) {
        try {
            Map<String, Object> claims = jwtService.verifyAndParseClaims(request.getRefreshToken());
            long userId = Long.parseLong(claims.get("userId").toString());
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new AuthenticationFailException("Invalid token"));

            TokensPair tokens = jwtService.generateTokens(user, accessExpiresIn, refreshExpiresIn);
            return new AuthUserResponse(
                    tokens.getAccessToken(),
                    DateUtils.currentUTCSeconds() + accessExpiresIn,
                    tokens.getRefreshToken()
            );
        } catch (JOSEException e) {
            throw new InternalServerException(e.getMessage());
        }
    }
}
