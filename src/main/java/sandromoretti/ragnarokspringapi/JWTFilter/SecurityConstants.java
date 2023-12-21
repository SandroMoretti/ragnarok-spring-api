package sandromoretti.ragnarokspringapi.JWTFilter;

public class SecurityConstants {
    public static final String JWT_SECRET = "B4T4T4";
    public static final long JWT_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 5;                     // 5 days
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER_STRING = "Authorization";
    public static final long USER_ACTION_LINK_PASSWORD_RESET_EXPIRATION_TIME = 1000 * 60 * 30;  // 30 minutes
}
