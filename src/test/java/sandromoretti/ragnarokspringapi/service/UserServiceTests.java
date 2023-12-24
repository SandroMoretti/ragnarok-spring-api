package sandromoretti.ragnarokspringapi.service;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sandromoretti.ragnarokspringapi.entity.User;
import sandromoretti.ragnarokspringapi.repository.UserRepository;
import sandromoretti.ragnarokspringapi.request.UserSignUpRequest;
import sandromoretti.ragnarokspringapi.response.UserSignUpResponse;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;


    @Mock
    private MessageSource messageSource;

    @Mock
    private MailService mailService;

    @InjectMocks
    private UserService userService;

    @Test
    public void when_save_user_should_return_200(){
        UserSignUpRequest userRequest = new UserSignUpRequest();
        userRequest.setEmail("test@test.com");
        userRequest.setUserid("test");
        userRequest.setUser_pass("test123");

        User default_created_user = new User();
        default_created_user.setUser_pass(userRequest.getUser_pass());
        default_created_user.setUserid(userRequest.getUserid());
        default_created_user.setEmail(userRequest.getEmail());
        default_created_user.setAccount_id(2000001);

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(default_created_user);
        ResponseEntity<UserSignUpResponse> response = userService.signUp(userRequest);

        assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));  // 200 ok
        assertThat(response.getBody().getToken(), Matchers.notNullValue());     // returning jwt token after register
        assertThat(response.getBody().getUser().getEmail(), Matchers.equalTo(userRequest.getEmail()));  // returning the registered user
    }

    @Test
    public void when_save_user_with_repeated_email_should_return_409(){
        // return conflict when save user with repeated email / userid
        UserSignUpRequest userRequest = new UserSignUpRequest();
        userRequest.setEmail("test_existent@test.com");
        userRequest.setUserid("test_existent");
        userRequest.setUser_pass("test123");

        User existent_user = new User();
        existent_user.setAccount_id(2000000);
        userRequest.setEmail("test_existent@test.com");
        userRequest.setUserid("test_existent");
        userRequest.setUser_pass("test123");

        Mockito.when(userRepository.findByEmail("test_existent@test.com")).thenReturn(existent_user);
        //Mockito.when(userRepository.findByUserid("test_existent")).thenReturn(existent_user);

        ResponseEntity<UserSignUpResponse> response = userService.signUp(userRequest);

        assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.CONFLICT));  // 200 ok
        assertThat(response.getBody().getToken(), Matchers.nullValue());     // returning jwt token after register
        assertThat(response.getBody().getUser(), Matchers.nullValue());  // returning the registered user
    }


    @Test
    public void when_save_user_with_repeated_userid_should_return_409(){
        // return conflict when save user with repeated email / userid
        UserSignUpRequest userRequest = new UserSignUpRequest();
        userRequest.setEmail("test_existent@test.com");
        userRequest.setUserid("test_existent");
        userRequest.setUser_pass("test123");

        User existent_user = new User();
        existent_user.setAccount_id(2000000);
        userRequest.setEmail("test_existent@test.com");
        userRequest.setUserid("test_existent");
        userRequest.setUser_pass("test123");

        Mockito.when(userRepository.findByUserid("test_existent")).thenReturn(existent_user);

        ResponseEntity<UserSignUpResponse> response = userService.signUp(userRequest);

        assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.CONFLICT));  // 200 ok
        assertThat(response.getBody().getToken(), Matchers.nullValue());     // returning jwt token after register
        assertThat(response.getBody().getUser(), Matchers.nullValue());  // returning the registered user
    }


}
