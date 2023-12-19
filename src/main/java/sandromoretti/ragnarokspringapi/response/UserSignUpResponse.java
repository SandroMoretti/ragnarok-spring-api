package sandromoretti.ragnarokspringapi.response;

import sandromoretti.ragnarokspringapi.entity.User;

public class UserSignUpResponse {
    private User user;
    private String message;
    private String token;

    public UserSignUpResponse(User user, String message, String token){
        this.setUser(user);
        this.setMessage(message);
        this.setToken(token);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
