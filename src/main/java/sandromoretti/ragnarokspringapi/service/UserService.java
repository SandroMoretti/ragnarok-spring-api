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
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import sandromoretti.ragnarokspringapi.JWTFilter.SecurityConstants;
import sandromoretti.ragnarokspringapi.config.GroupsConfig;
import sandromoretti.ragnarokspringapi.entity.User;
import sandromoretti.ragnarokspringapi.repository.UserRepository;
import sandromoretti.ragnarokspringapi.request.UserSignInRequest;
import sandromoretti.ragnarokspringapi.response.UserSignInResponse;
import sandromoretti.ragnarokspringapi.response.UserSignUpResponse;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;


    public Optional<User> getUserById(int id){
        return userRepository.findById(id);
    }

    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    public String getJWTToken(User user){
        String token =
                JWT.create()
                        .withSubject(user.getUserid())
                        .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                        .sign(Algorithm.HMAC256(SecurityConstants.SECRET));
        return token;
    }

    public ResponseEntity<UserSignUpResponse> signUp(User user){
        Locale locale = LocaleContextHolder.getLocale();

        if(user.getGroup_id() != GroupsConfig.PLAYER_GROUP_ID){
            String err_message = messageSource.getMessage("user.signup.error_invalid_group_id", null, locale);
            return new ResponseEntity<>(new UserSignUpResponse(null, err_message, null), HttpStatus.BAD_REQUEST);
        }
        if(userRepository.findByUserid(user.getUserid()) != null){
            String err_message = messageSource.getMessage("user.signup.error_repeated.userid", null, locale);
            return new ResponseEntity<>(new UserSignUpResponse(null, err_message, null), HttpStatus.CONFLICT);
        }
        if(userRepository.findByEmail(user.getEmail()) != null){
            String err_message = messageSource.getMessage("user.signup.error_repeated.email", null, locale);
            return new ResponseEntity<>(new UserSignUpResponse(null, err_message, null), HttpStatus.CONFLICT);
        }

        // all validations OK
        User saved_user = userRepository.save(user);
        this.logger.info("New user: " + saved_user.toString());
        String message = messageSource.getMessage(saved_user != null ? "user.signup.create_success" : "user.signup.error_unknown_reason", null, locale);
        String token = saved_user != null ? getJWTToken(saved_user) : null;
        return new ResponseEntity<>(new UserSignUpResponse(saved_user, message, token), saved_user != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<UserSignInResponse> signIn(UserSignInRequest userSignInRequest){
        UserSignInResponse response = new UserSignInResponse();
        User user = userRepository.findByUserid(userSignInRequest.getUserid());
        if (user != null) {
            String pass_md5 = DigestUtils.md5DigestAsHex(userSignInRequest.getUser_pass().getBytes(StandardCharsets.UTF_8));

            if (pass_md5.equalsIgnoreCase(user.getUser_pass())) {
                /*LocalDate date = LocalDate.parse("2199-12-01");
                Instant infinity_date = date.atStartOfDay(ZoneId.of("Europe/Paris")).toInstant();
                // uncomment only for no expire tokens (not recommended)
                 */

                response.setToken(
                        getJWTToken(user));
                response.setUser(user);
            }
        }


        return new ResponseEntity<UserSignInResponse>(response, response.getToken() != null ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
    }
}
