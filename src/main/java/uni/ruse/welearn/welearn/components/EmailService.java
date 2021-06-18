package uni.ruse.welearn.welearn.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @author ivelin.dimitrov
 */
@Component
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendSimpleMessage(
            String to, String subject, String text
    ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@welearn.bg");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            log.info("Sending email to " + to + " with subject: " + subject);
            javaMailSender.send(message);
            log.info("Successfully sent email to " + to + " with subject: " + subject);
        }catch (Exception e){
            log.info("Failed sending email to " + to + " with subject: " + subject);
            e.printStackTrace();
        }
    }
}
