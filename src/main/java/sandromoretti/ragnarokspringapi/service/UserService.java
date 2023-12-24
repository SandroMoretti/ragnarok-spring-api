package sandromoretti.ragnarokspringapi.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import sandromoretti.ragnarokspringapi.JWTFilter.SecurityConstants;
import sandromoretti.ragnarokspringapi.config.GroupsConfig;
import sandromoretti.ragnarokspringapi.entity.User;
import sandromoretti.ragnarokspringapi.entity.UserActionToken;
import sandromoretti.ragnarokspringapi.repository.UserActionTokenRepository;
import sandromoretti.ragnarokspringapi.repository.UserRepository;
import sandromoretti.ragnarokspringapi.request.UserChangePasswordRequest;
import sandromoretti.ragnarokspringapi.request.UserPasswordResetRequest;
import sandromoretti.ragnarokspringapi.request.UserSignInRequest;
import sandromoretti.ragnarokspringapi.request.UserSignUpRequest;
import sandromoretti.ragnarokspringapi.response.UserChangePasswordResponse;
import sandromoretti.ragnarokspringapi.response.UserPasswordResetResponse;
import sandromoretti.ragnarokspringapi.response.UserSignInResponse;
import sandromoretti.ragnarokspringapi.response.UserSignUpResponse;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserActionTokenRepository userActionTokenRepository;

    @Autowired
    MailService mailService;

    @Autowired
    private MessageSource messageSource;


    public Optional<User> getUserById(int id){
        return userRepository.findById(id);
    }

    public User getUserByJWTSubject(String subject){
        return userRepository.findByUserid(subject);
    }

    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    public String getJWTToken(User user){
        String token =
                JWT.create()
                        .withSubject(user.getUserid())
                        .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.JWT_EXPIRATION_TIME))
                        .sign(Algorithm.HMAC256(SecurityConstants.JWT_SECRET));
        return token;
    }

    public ResponseEntity<UserPasswordResetResponse> sendPasswordResetEmail(UserPasswordResetRequest userPasswordResetRequest){
        Locale locale = LocaleContextHolder.getLocale();

        User user = this.userRepository.findByEmail(userPasswordResetRequest.getEmail());
        String message = messageSource.getMessage("user.password_reset.success", null, locale);

        if(user != null) {
            // all validations OK
            UserActionToken userActionToken = new UserActionToken();
            userActionToken.setUser(user);
            userActionToken.setAction(UserActionToken.ACTION_TOKEN_PASSWORD_RESET);
            userActionToken.setExpires_at(new Timestamp(System.currentTimeMillis() + SecurityConstants.USER_ACTION_LINK_PASSWORD_RESET_EXPIRATION_TIME));

            UserActionToken saved_userActionToken = this.userActionTokenRepository.save(userActionToken);

            if (saved_userActionToken != null) {
                this.mailService.sendPasswordResetMail(user.getEmail(), user.getUserid(), user.getAccount_id(), saved_userActionToken.getToken(), saved_userActionToken.getExpires_at());
            }
        }

        return new ResponseEntity<>(new UserPasswordResetResponse(message), HttpStatus.ACCEPTED);
    }

    public ResponseEntity<UserChangePasswordResponse> changePassword(int account_id, UserChangePasswordRequest userChangePasswordRequest){
        Locale locale = LocaleContextHolder.getLocale();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        User user = this.userRepository.findById(account_id).orElseThrow();
        User authenticated_user = null;
        if(authentication.getPrincipal() != null && !authentication.getPrincipal().equals("anonymousUser")) {
            // jwt authenticated method
            this.logger.debug("principal: " + authentication.getPrincipal().toString());
            authenticated_user = ((User) authentication.getPrincipal());
        }else if(userChangePasswordRequest.getToken() != null){
            // reset password method
            UserActionToken token = this.userActionTokenRepository.findValidTokenFromUser(userChangePasswordRequest.getToken(), account_id);
            if(token == null) { // invalid token, maybe expired
                return new ResponseEntity<>(new UserChangePasswordResponse(messageSource.getMessage("user.change_password.error_invalid_token", null, locale)), HttpStatus.UNAUTHORIZED);
            }

            if(token != null && token.getAction().equals(UserActionToken.ACTION_TOKEN_PASSWORD_RESET)){
                // valid reset password token
                authenticated_user = user;
                token.setUsed(true);
                this.userActionTokenRepository.save(token);
            }
        }

        if(authenticated_user == null || authenticated_user.getAccount_id() != account_id){ // check if user is authenticated but sending an random account_id
            return new ResponseEntity<>(new UserChangePasswordResponse(messageSource.getMessage("user.change_password.error", null, locale)), HttpStatus.BAD_REQUEST);
        }


        authenticated_user.setUser_pass(DigestUtils.md5DigestAsHex(userChangePasswordRequest.getPassword().getBytes(StandardCharsets.UTF_8)));
        this.userRepository.save(authenticated_user);

        String message = messageSource.getMessage("user.change_password.success", null, locale);
        return new ResponseEntity<>(new UserChangePasswordResponse(message), HttpStatus.OK);
    }

    public ResponseEntity<UserSignUpResponse> signUp(UserSignUpRequest userRequest){
        Locale locale = LocaleContextHolder.getLocale();

        if(userRepository.findByUserid(userRequest.getUserid()) != null){
            String err_message = messageSource.getMessage("user.signup.error_repeated.userid", null, locale);
            return new ResponseEntity<>(new UserSignUpResponse(null, err_message, null), HttpStatus.CONFLICT);
        }
        if(userRepository.findByEmail(userRequest.getEmail()) != null){
            String err_message = messageSource.getMessage("user.signup.error_repeated.email", null, locale);
            return new ResponseEntity<>(new UserSignUpResponse(null, err_message, null), HttpStatus.CONFLICT);
        }

        // all validations OK
        userRequest.setUser_pass(DigestUtils.md5DigestAsHex(userRequest.getUser_pass().getBytes(StandardCharsets.UTF_8)));

        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setUserid(userRequest.getUserid());
        user.setUser_pass(userRequest.getUser_pass());
        User saved_user = userRepository.save(user);
        this.logger.info("New user: " + saved_user.toString());
        String message = messageSource.getMessage(saved_user != null ? "user.signup.create_success" : "user.signup.error_unknown_reason", null, locale);
        String token = saved_user != null ? getJWTToken(saved_user) : null;
        if(saved_user != null){
            this.mailService.sendSignupMail(saved_user.getEmail(), saved_user.getUserid());
        }
        return new ResponseEntity<>(new UserSignUpResponse(saved_user, message, token), saved_user != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<UserSignInResponse> signIn(UserSignInRequest userSignInRequest){
        UserSignInResponse response = new UserSignInResponse();
        User user = userRepository.findByUserid(userSignInRequest.getUserid());
        if (user != null) {
            String pass_md5 = DigestUtils.md5DigestAsHex(userSignInRequest.getUser_pass().getBytes(StandardCharsets.UTF_8));

            if (pass_md5.equalsIgnoreCase(user.getUser_pass())) {
                response.setToken(
                        getJWTToken(user));
                response.setUser(user);
            }
        }


        return new ResponseEntity<UserSignInResponse>(response, response.getToken() != null ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
    }
}
