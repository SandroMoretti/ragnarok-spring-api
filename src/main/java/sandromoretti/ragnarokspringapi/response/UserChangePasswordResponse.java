package sandromoretti.ragnarokspringapi.response;

public class UserChangePasswordResponse {
    private String message;

    public UserChangePasswordResponse(String message) {
        this.setMessage(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
