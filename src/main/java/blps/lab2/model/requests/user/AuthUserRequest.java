package blps.lab2.model.requests.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthUserRequest {
    private String email;
    private String password;
}
