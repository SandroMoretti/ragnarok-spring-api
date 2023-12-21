package sandromoretti.ragnarokspringapi.request;

import jakarta.validation.constraints.NotBlank;

public class UserPasswordResetRequest {
    @NotBlank
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
