package Project.OrderManagement.server.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendVerificationEmail(String email, String token){

        final String emailContent = generateEmail(token);
        MimeMessage message = javaMailSender.createMimeMessage();

        try{
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
            helper.setTo(email);
            helper.setSubject("Verify your email");
            helper.setText(emailContent, true);
            javaMailSender.send(message);
        }catch (MessagingException e){
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private static String generateEmail(String token) {
        String verificationLink = "http://localhost:8080/verify-email?token=" + token;

        return "<html lang=\"en\"><head>...</head><body>" +
                "<div class=\"container\"><h2>Welcome to Our Service!</h2>" +
                "<p>Thank you for registering. Please verify your email address by clicking the button below:</p>" +
                "<a href=\"" + verificationLink + "\" class=\"button\">Verify Email</a>" +
                "<p>If you did not sign up for this account, please ignore this email.</p>" +
                "<div class=\"footer\"><p>© 2024 Your Company. All rights reserved.</p></div>" +
                "</div></body></html>";
    }
}
