package com.anillohar.usermanagement.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailUtils {

    @Autowired
    private JavaMailSender javaMailSender;

    public boolean sendEmail(String to,
                             String subject,
                             String body) {

        try {

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            javaMailSender.send(message);

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}