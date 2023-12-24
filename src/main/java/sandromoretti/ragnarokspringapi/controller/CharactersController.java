package sandromoretti.ragnarokspringapi.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sandromoretti.ragnarokspringapi.config.GroupsConfig;
import sandromoretti.ragnarokspringapi.entity.User;

@RestController
public class CharactersController {

    @PreAuthorize(value = "authentication.principal.account_id == #account_id")
    @GetMapping(path="/users/{account_id}/characters")
    public String getAllUsers(@PathVariable int account_id){
        return "test: " + account_id;
    }
}
