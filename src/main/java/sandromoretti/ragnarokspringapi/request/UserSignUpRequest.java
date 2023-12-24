package sandromoretti.ragnarokspringapi.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
public class UserSignUpRequest {

    @NotBlank(message = "Informe um e-mail valido")
    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Informe um login valido")
    @Column(name = "userid", nullable = false, unique = true)
    private String userid;

    @NotBlank(message = "Informe uma senha valida")
    @Size(min = 6, max = 32)
    private String user_pass;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }

    @Override
    public String toString(){
        return this.getEmail() + "/" + this.getUserid();
    }
}
