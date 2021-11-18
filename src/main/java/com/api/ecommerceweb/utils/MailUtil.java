package com.api.ecommerceweb.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Component("MailUtil")
@RequiredArgsConstructor
public class MailUtil {

    private final JavaMailSender javaMailSender;
    private final String FROM_ADDRESS = "testting7989@gmail.com";
    private final String SENDER_NAME = "Sheppo Ecommerce";

    public void sendVerificationCode(String receivedEmail, String verificationCode) throws MessagingException, UnsupportedEncodingException {
        final String subject = "Account Verification Code";
        String content =
                "Your verification code is:<br>"
                        + "<h2 style=\"color: blueviolet; font-weight: bolder\">[[code]]</h2>"
                        + "Thank you,<br>"
                        + "Have a good experience in Sheppo Ecommerce.";
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom(FROM_ADDRESS, SENDER_NAME);
        mimeMessageHelper.setTo(receivedEmail);
        mimeMessageHelper.setSubject(subject);
        content = content.replace("[[code]]", verificationCode);
        mimeMessageHelper.setText(content, true);
        javaMailSender.send(mimeMessage);
    }


    public String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }

}
