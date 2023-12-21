package sandromoretti.ragnarokspringapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sandromoretti.ragnarokspringapi.entity.User;
import sandromoretti.ragnarokspringapi.config.GroupsConfig;
import sandromoretti.ragnarokspringapi.request.UserChangePasswordRequest;
import sandromoretti.ragnarokspringapi.request.UserPasswordResetRequest;
import sandromoretti.ragnarokspringapi.response.UserChangePasswordResponse;
import sandromoretti.ragnarokspringapi.response.UserPasswordResetResponse;
import sandromoretti.ragnarokspringapi.response.UserSignUpResponse;
import sandromoretti.ragnarokspringapi.service.UserService;
import sandromoretti.ragnarokspringapi.request.UserSignInRequest;
import sandromoretti.ragnarokspringapi.response.UserSignInResponse;


@Tag(name="Users", description = "Users api")
@RestController
@RequestMapping(path="/users")
public class UserController {
    @Autowired
    UserService userService;

    @RolesAllowed(GroupsConfig.ADMIN)
    @GetMapping(path="")
    public Iterable<User> getAllUsers(){
        return userService.getAllUsers();
    }



    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PreAuthorize(value = "authentication.principal.account_id == #id or hasAnyRole('"+ GroupsConfig.ADMIN+"', '"+ GroupsConfig.GAME_MASTER+"')")
    @GetMapping(path="/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id){
        User user =  userService.getUserById(id).orElseThrow();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(path="/password-reset")
    public ResponseEntity<UserPasswordResetResponse> passwordResetEmail(@RequestBody @Valid UserPasswordResetRequest userPasswordResetRequest){
        return this.userService.sendPasswordResetEmail(userPasswordResetRequest);
    }

    @PatchMapping(path="/{account_id}/password")
    public ResponseEntity<UserChangePasswordResponse> changePassword(@PathVariable int account_id, @RequestBody @Valid UserChangePasswordRequest userChangePasswordRequest){
        return this.userService.changePassword(account_id, userChangePasswordRequest);
    }

    @PostMapping(path="")
    public ResponseEntity<UserSignUpResponse> signUp(@RequestBody @Valid User user){
        return userService.signUp(user);
    }

    @PostMapping(path="/sign-in")
    public ResponseEntity<UserSignInResponse> signIn(@RequestBody @Valid UserSignInRequest userSignInRequest){
        return userService.signIn(userSignInRequest);
    }
}
