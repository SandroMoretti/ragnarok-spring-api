package sandromoretti.ragnarokspringapi.JWTFilter;

public class SecurityConstants {
    public static final String SECRET = "B4T4T4";
    public static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 5; // 5 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
