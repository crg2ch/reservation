package com.minsoo.reservation.common;

import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MailComponent {
    public final JavaMailSender javaMailSender;

    public void sendMail(String fromEmail, String fromName,
                         String toEmail, String toName,
                         String title, String contents) {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            InternetAddress from = new InternetAddress();
            from.setAddress(fromEmail);
            from.setPersonal(fromName);

            InternetAddress to = new InternetAddress();
            to.setAddress(toEmail);
            to.setPersonal(toName);

            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(contents, true);
        };
        try {
            javaMailSender.send(mimeMessagePreparator);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}
