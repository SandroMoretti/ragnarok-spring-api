package sandromoretti.ragnarokspringapi.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
public class UserChangePasswordRequest {
    private String token;

    @NotBlank(message = "Informe uma senha valida")
    @Size(min=6, max=32)
    private String password;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
