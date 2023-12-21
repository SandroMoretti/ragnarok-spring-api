package sandromoretti.ragnarokspringapi.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sandromoretti.ragnarokspringapi.service.MailService;

import java.time.Instant;

@Tag(name="Mail (sender)", description = "Use only to send email templates, helping create custom html mail pages or to send custom mails from panel admin")
@Controller
@RequestMapping(path="/mail")
public class MailController {
    @Autowired
    MailService mailService;

    @GetMapping(path="/test")
    public String test(Model model){

        model.addAttribute("username", "ragjr2");
        model.addAttribute("usermail", "jniorsandro2@gmail.com");
        model.addAttribute("site_url_home", "https://nostalgiaro.com.br");
        model.addAttribute("site_url_password_reset", "https://nostalgiaro.com.br/password-reset");
        model.addAttribute("date_format", "dd/MM/yyyy 'às' HH:mm");
        model.addAttribute("expires_at", Instant.now());
        model.addAttribute("server_name", "NostalgiaRO");

        return "email/password-reset.html";
    }

    @GetMapping(path="/test-signup-mail")
    public void testSignupMail(Model model){
        model.addAttribute("username", "ragjr");
        model.addAttribute("usermail", "jniorsandro@gmail.com");

        this.mailService.sendSignupMail("jniorsandro2@gmail.com", "ragjr");
    }


}
