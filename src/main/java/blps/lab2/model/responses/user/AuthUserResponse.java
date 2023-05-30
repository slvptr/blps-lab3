package blps.lab2.model.responses.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthUserResponse {
    private String accessToken;
    private Long expiresIn;
    private String refreshToken;
}
