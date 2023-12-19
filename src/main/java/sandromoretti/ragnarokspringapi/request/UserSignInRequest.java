package sandromoretti.ragnarokspringapi.request;

import jakarta.validation.constraints.NotBlank;

public class UserSignInRequest {
    @NotBlank
    private String userid;

    @NotBlank
    private String user_pass;

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
}
