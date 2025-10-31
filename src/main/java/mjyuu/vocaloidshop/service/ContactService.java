package mjyuu.vocaloidshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final JavaMailSender mailSender;

    @Value("${contact.to:neneke.emu@gmail.com}")
    private String contactTo;

    @Value("${spring.mail.username:}")
    private String defaultFrom;

    public void send(String senderName, String senderEmail, String title, String details) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(contactTo);
        if (defaultFrom != null && !defaultFrom.isBlank()) {
            msg.setFrom(defaultFrom);
        }
        msg.setSubject("[Contact] " + title);
        String body = "From: " + senderName + " <" + senderEmail + ">\n\n" + details;
        msg.setText(body);
        mailSender.send(msg);
    }
}
