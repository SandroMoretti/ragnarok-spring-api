package sandromoretti.ragnarokspringapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTestEmail(String email, String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jniorsandro@gmail.com");
        message.setSubject("Mensagem de teste");
        message.setText(content);
        message.setTo(email);

        mailSender.send(message);
    }
}
