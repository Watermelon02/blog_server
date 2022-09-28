package com.blog.util;

import com.blog.service.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class EmailUtil {
    @Autowired
    private SendEmailService sendEmailService;

    /**
     * 发送邮件
     */
    @Async("asyncThreadPoolTaskExecutor")
    public void sendMail(String alias,String email,String subject,String content) {
        sendEmailService.send(alias,email,subject,content,new ArrayList<>());
    }
}
