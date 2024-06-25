package com.akshit.treading.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sandVerificationOtpEmail(String email, String otp) throws MessagingException {
        SimpleMailMessage mail = new SimpleMailMessage();
        String subject = "Otp Verification Email";
        String text = "your Verification Code is : " + otp;

        mail.setText(text);
        mail.setSubject(subject);
        mail.setTo(email);

        try {
            javaMailSender.send(mail);
        } catch (Exception e) {
            throw new MailSendException(e.getMessage());
        }
    }
}
