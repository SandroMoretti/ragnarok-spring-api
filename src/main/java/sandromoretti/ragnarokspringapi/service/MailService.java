package sandromoretti.ragnarokspringapi.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Locale;

@Service
public class MailService {
    @Value("${mail.from}")
    private String mailFrom;

    @Value("${ragnarok.server_name}")
    private String serverName;

    @Value("${ragnarok.site_url.home}")
    private String siteUrlHome;

    @Value("${ragnarok.site_url.download}")
    private String siteUrlDownload;

    @Value("${ragnarok.site_url.password_reset}")
    private String siteUrlPasswordReset;

    @Value("${custom_configs.date_format}")
    private String dateFormat;

    @Autowired
    private MessageSource messageSource;


    Logger logger = LoggerFactory.getLogger(MailService.class);


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Async
    public void sendSignupMail(String usermail, String username) {
        try {
            this.logger.info("Signup mail send - started");
            Locale locale = LocaleContextHolder.getLocale();

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            mimeMessageHelper.setFrom(mailFrom);
            mimeMessageHelper.setTo(usermail);
            mimeMessageHelper.setSubject(messageSource.getMessage("mail.signup.welcome", null, locale) + " " + serverName);

            Context context = new Context();

            context.setVariable("username", username);
            context.setVariable("usermail", usermail);
            context.setVariable("site_url_home", siteUrlHome);
            context.setVariable("site_url_download", siteUrlDownload);
            context.setVariable("server_name", serverName);

            String processedString = templateEngine.process("email/signup", context);

            mimeMessageHelper.setText(processedString, true);
            mailSender.send(mimeMessage);

            this.logger.info("Signup mail send - finished");
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    @Async
    public void sendPasswordResetMail(String usermail, String username, int account_id, String token, Timestamp expires_at) {
        try {
            this.logger.info("Password Reset mail send - started");
            Locale locale = LocaleContextHolder.getLocale();

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            mimeMessageHelper.setFrom(mailFrom);
            mimeMessageHelper.setTo(usermail);
            mimeMessageHelper.setSubject(serverName + " - " + messageSource.getMessage("mail.password_reset.reset_your_password", null, locale));

            Context context = new Context();

            context.setVariable("username", username);
            context.setVariable("usermail", usermail);
            context.setVariable("site_url_home", siteUrlHome);
            context.setVariable("site_url_password_reset", siteUrlPasswordReset + "?account_id=" + account_id + "&token=" + token);
            context.setVariable("date_format", dateFormat);
            context.setVariable("expires_at", expires_at.toInstant());
            context.setVariable("server_name", serverName);

            String processedString = templateEngine.process("email/password-reset", context);

            mimeMessageHelper.setText(processedString, true);
            mailSender.send(mimeMessage);

            this.logger.info("Password Reset mail send - finished");
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Send an html email using the html page as base, replacing all variables from ${variable} with args hashmap values.
     */
    public void sendHtmlEmail(String email, String subject, String html_page, HashMap<String, String> args) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress(mailFrom));
        message.setSubject(subject);
        message.setRecipients(MimeMessage.RecipientType.TO, email);
        message.setContent(html_page, "text/html; charset=utf-8");

        mailSender.send(message);
    }
}
