package com.expmanagment.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class SendEmailUtil {

    @Value("${mail.from.addr}")
    private String from;

    @Autowired
    JavaMailSender javaMailSender;

    public void sendSimpleMail(String to, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        //收信人
        message.setTo(to);
        //主题
        message.setSubject(title);
        //内容
        message.setText(content);
        //发信人
        message.setFrom(from);

        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendHtmlMail(String to, String title, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(content, true);   //true代表支持html
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
