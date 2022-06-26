package com.api.ecommerceweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Random;

@Component("MailService")
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final String FROM_ADDRESS = "testting7989@gmail.com";
    private final String SENDER_NAME = "Sheppo Ecommerce";

    public String sendVerificationCode(String receivedEmail) throws MessagingException, UnsupportedEncodingException {
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

        String code = generateVerificationCode();
        content = content.replace("[[code]]", code);
        mimeMessageHelper.setText(content, true);
        javaMailSender.send(mimeMessage);
        return code;
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
    public String sendActiveShopCode(String receivedEmail) throws MessagingException, UnsupportedEncodingException {
        final String subject = "Account Verification Code";
        String content =
                "Your active shop code is:<br>"
                        + "<h2 style=\"color: blueviolet; font-weight: bolder\">[[code]]</h2>"
                        + "Thank you for open a shop,<br>"
                        + "Have a good experience in Sheppo Seller Center.";
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom(FROM_ADDRESS, SENDER_NAME);
        mimeMessageHelper.setTo(receivedEmail);
        mimeMessageHelper.setSubject(subject);

        String code = generateVerificationCode();
        content = content.replace("[[code]]", code);
        mimeMessageHelper.setText(content, true);
        javaMailSender.send(mimeMessage);
        return code;
    }

}
