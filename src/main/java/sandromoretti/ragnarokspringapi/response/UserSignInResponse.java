package sandromoretti.ragnarokspringapi.response;

import sandromoretti.ragnarokspringapi.entity.User;

public class UserSignInResponse {
    private User user;
    private String token;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
