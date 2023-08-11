package io.github.kleberrhuan.butcherapp.infra.config.mail;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.kleberrhuan.butcherapp.domain.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final Dotenv dotenv;
    private final TemplateEngine templateEngine;


     public void sendEmail(String email, String subject, String text) {
         SimpleMailMessage message = new SimpleMailMessage();
         message.setTo(email);
         message.setSubject(subject);
         message.setText(text);
         mailSender.send(message);
    }

    public void sendResetEmail(User user){

     try {

        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getFullName());
        model.put("host", this.getHost());
        model.put("resetCode", user.getResetCode());
        model.put("email", user.getEmail());
        model.put("imageUrl", "https://yourimageshare.com/ib/Tou3qpTLnS" );

        Context context = new Context();
        context.setVariables(model);
        context.setLocale(Locale.forLanguageTag("pt-BR"));

        String htmlContent = templateEngine.process("reset-email", context);

        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject("Reset Password");
            messageHelper.setText(htmlContent, true);
        };

            mailSender.send(mimeMessagePreparator);
    } catch (Exception e) {
           throw new InternalError("Error sending email" + e.getMessage());
        }

    }

    public void sendRegistrationEmail(User user){

            try {

                Map<String, Object> model = new HashMap<>();
                model.put("name", user.getFullName());
                model.put("verificationLink", getVerificationLink(user.getVerificationCode()));

                Context context = new Context();
                context.setVariables(model);
                context.setLocale(Locale.forLanguageTag("pt-BR"));

                String htmlContent = templateEngine.process("register-email", context);
                MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
                    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                    messageHelper.setTo(user.getEmail());
                    messageHelper.setSubject("Verifique seu email");
                    messageHelper.setText(htmlContent, true);
                };

                mailSender.send(mimeMessagePreparator);
            } catch (Exception e) {
                throw new InternalError("Error sending email" + e.getMessage());
            }
    }


    public String getHost(){
        return dotenv.get("HOST");
    }

    public String getVerificationLink(String verificationCode){
        return String.format("%s/verify/%s", getHost(), verificationCode);
    }


}
