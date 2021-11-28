package com.api.ecommerceweb.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
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


    public void sendCancelOrderInfo(String to, String orderId, String orderUserName, Date cancelDate, String reason) throws MessagingException, UnsupportedEncodingException {
        String content =
                "Cancel Order:<br>"
                        + "<h2 style=\"color: blueviolet; font-weight: bolder\">" +
                        "The order [[orderId]] have just canceled by [[orderUserName]] at [[cancelDate]]" + "<br>" +
                        "Reason: " + "[[reason]]" +
                        "</h2>"
                        + "Thank you,<br>"
                        + "Have a good experience in Sheppo Ecommerce.";
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom(FROM_ADDRESS, to);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject("Canceled order #" + orderId);
        content = content.replace("[[orderId]]", orderId);
        content = content.replace("[[orderUserName]]", orderUserName);
        content = content.replace("[[cancelDate]]", cancelDate.toString());
        content = content.replace("[[reason]]", reason);
        mimeMessageHelper.setText(content, true);
        javaMailSender.send(mimeMessage);


    }

}
