package blps.lab2.controller.user;

import blps.lab2.model.domain.user.User;
import blps.lab2.model.domain.user.UserRole;
import blps.lab2.model.requests.user.AuthUserRequest;
import blps.lab2.model.requests.user.RefreshUserRequest;
import blps.lab2.model.responses.user.AuthUserResponse;
import blps.lab2.model.responses.user.UserView;
import blps.lab2.service.user.AuthService;
import blps.lab2.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    private AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public UserView register(@RequestBody @Valid AuthUserRequest req) {
        User user = authService.register(req);
        return UserView.fromUser(user);
    }

    @PostMapping("/login")
    public AuthUserResponse login(@RequestBody @Valid AuthUserRequest req) {
        return authService.login(req);
    }

    @PostMapping("/refresh")
    public AuthUserResponse refresh(@RequestBody @Valid RefreshUserRequest req) {
        return authService.refresh(req);
    }
}
