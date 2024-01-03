package com.jkbilibili.utils.email;


import com.jkbilibili.exception.BusinessException;
import com.jkbilibili.exception.BusinessExceptionEnum;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EMailUtils {


    private final static Logger LOG = LoggerFactory.getLogger(EMailUtils.class);

    @Value("${spring.mail.username}")
    private String from;   // 邮件发送人

    @Autowired
    private JavaMailSender mailSender;

    public void sendEMail(String to, String subject, String context) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(context);

        try {
            // 真正的发送邮件操作，从 from到 to
            mailSender.send(mailMessage);
        } catch (Exception e) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_EMAIL_SEND_ERROR);
        }

    }

}

