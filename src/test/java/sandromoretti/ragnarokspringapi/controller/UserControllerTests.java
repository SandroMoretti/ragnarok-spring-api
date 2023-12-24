package sandromoretti.ragnarokspringapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import sandromoretti.ragnarokspringapi.ControllerAdvisor;
import sandromoretti.ragnarokspringapi.JWTFilter.JWTAuthenticationFilter;
import sandromoretti.ragnarokspringapi.config.SecurityConfiguration;
import sandromoretti.ragnarokspringapi.entity.User;
import sandromoretti.ragnarokspringapi.request.UserSignUpRequest;
import sandromoretti.ragnarokspringapi.response.UserSignUpResponse;
import sandromoretti.ragnarokspringapi.service.UserService;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ContextConfiguration(classes={UserController.class, ControllerAdvisor.class, JWTAuthenticationFilter.class, SecurityConfiguration.class})
@WebMvcTest(UserController.class)
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    ObjectMapper objectMapper;




    @Test
    public void testAddShouldReturn400BadRequest() throws Exception{
        /*test without any user detail*/
        UserSignUpRequest user = new UserSignUpRequest();
        user.setEmail("test@test.com");
        user.setUserid("test");
        user.setUser_pass("test123");

        /*test without email*/
        user.setEmail("");
        mockMvc.perform(post("/users").contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andDo(print());
        user.setEmail("test@test.com");

        /*test without userid*/
        user.setUserid("");
        mockMvc.perform(post("/users").contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andDo(print());
        user.setUserid("test");

        /*test with password*/
        user.setUser_pass("");
        mockMvc.perform(post("/users").contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andDo(print());
        user.setUser_pass("test123");
    }

    @Test
    public void testAddShouldReturn200OK() throws Exception{
        UserSignUpRequest user = new UserSignUpRequest();
        user.setEmail("test@test.com");
        user.setUserid("test");
        user.setUser_pass("test123");


        Mockito.when(userService.signUp(Mockito.any())).thenReturn(new ResponseEntity<>(new UserSignUpResponse(new User(), "OK", "t0k3n"), HttpStatus.OK));

        mockMvc.perform(post("/users").contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", equalTo("t0k3n")))
                .andExpect(jsonPath("$.message", equalTo("OK")))
                .andDo(print());

        Mockito.verify(userService, times(1)).signUp(Mockito.any());
    }
}
