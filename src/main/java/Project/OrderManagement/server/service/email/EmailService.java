package Project.OrderManagement.server.service.email;

import Project.OrderManagement.server.model.VerificationLinkStatus;
import Project.OrderManagement.server.model.entity.ConfirmationToken;
import Project.OrderManagement.server.model.entity.UserEntity;
import Project.OrderManagement.server.model.repository.ConfirmationTokenRepository;
import Project.OrderManagement.server.model.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Transactional
    public void saveConfirmationToken(ConfirmationToken token){
        entityManager.persist(token);
    }

    @Transactional
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

    @Transactional
    private static String generateEmail(String token) {
        String templatePath = "src/main/resources/templates/email.html";
        String emailContent;

        try {
            emailContent = Files.readString(Paths.get(templatePath));

            String verificationLink = "http://localhost:8080/user/verify-email?token=" + token;
            emailContent = emailContent.replace("{{verificationLink}}", verificationLink);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load email template", e);
        }

        return emailContent;
    }

    @Transactional
    public VerificationLinkStatus verifyEmail(String token) {
        Optional<UserEntity> userOptional = userRepository.findUserByEmailVerificationToken(token);
        ConfirmationToken confirmationToken = confirmationTokenRepository.findEmailConfirmationToken(token).orElse(null);

        if(confirmationToken == null){
            return VerificationLinkStatus.INVALID_TOKEN;
        }

        if(confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())){
            return VerificationLinkStatus.TOKEN_EXPIRED;
        }

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
                user.setIsVerified(true);
                user.setEmailVerificationToken(null);
                userRepository.saveUser(user);
                return VerificationLinkStatus.SUCCESS;
        }

        return VerificationLinkStatus.INVALID_TOKEN;
    }
}
