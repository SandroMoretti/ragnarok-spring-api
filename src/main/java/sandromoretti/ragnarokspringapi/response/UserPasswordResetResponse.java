package sandromoretti.ragnarokspringapi.response;

import sandromoretti.ragnarokspringapi.entity.User;

public class UserPasswordResetResponse {
    private String message;

    public UserPasswordResetResponse(String message) {
        this.setMessage(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
