package com.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.blog.service.SendEmailService;
import com.sun.mail.util.MailSSLSocketFactory;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Service
public class SendEmailServiceImpl implements SendEmailService {
    @Value("${mail.account}")
    private  String account ;

    /**
     * 登录密码
     */
    @Value("${mail.password}")
    private  String password;

    /**
     * 发信协议
     */
    @Value("${mail.transport.protocol}")
    private String protocol;

    /**
     * 邮件服务器地址
     */
    @Value("${mail.smtp.host}")
    private String host;

    /**
     * 发信端口
     */
    @Value("${mail.smtp.port}")
    private String port ;

    /**
     * 发信端口
     */
    @Value("${mail.smtp.auth}")
    private String auth ;


    @Value("${mail.smtp.ssl.enable}")
    private String sslEnable ;



    /**
     * 发送邮件
     */
    @Override
    public void send(String fromAliasName, String to, String subject, String content, List<String> attachFileList) {
        // 设置邮件属性
        Properties prop = new Properties();
        prop.setProperty("mail.transport.protocol", protocol);
        prop.setProperty("mail.smtp.host", host);
        prop.setProperty("mail.smtp.port", port);
        prop.setProperty("mail.smtp.auth", auth);
        MailSSLSocketFactory sslSocketFactory = null;
        try {
            sslSocketFactory = new MailSSLSocketFactory();
            sslSocketFactory.setTrustAllHosts(true);
        } catch (GeneralSecurityException e1) {
            e1.printStackTrace();
        }
        if (sslSocketFactory == null) {
            System.out.println("开启 MailSSLSocketFactory 失败");
        } else {
            prop.put("mail.smtp.ssl.enable",sslEnable);
            prop.put("mail.smtp.ssl.socketFactory", sslSocketFactory);
            // 创建邮件会话（注意，如果要在一个进程中切换多个邮箱账号发信，应该用 Session.getInstance）
            Session session = Session.getDefaultInstance(prop, new MyAuthenticator(account, password));
            try {
                MimeMessage mimeMessage = new MimeMessage(session);
                // 设置发件人别名（如果未设置别名就默认为发件人邮箱）
                if (fromAliasName != null && !fromAliasName.trim().isEmpty()) {
                    mimeMessage.setFrom(new InternetAddress(account, fromAliasName));
                }
                // 设置主题和收件人、发信时间等信息
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                mimeMessage.setSubject(subject);
                mimeMessage.setSentDate(new Date());
                // 如果有附件信息，则添加附件
                Multipart multipart = new MimeMultipart();
                MimeBodyPart body = new MimeBodyPart();
                body.setContent(content, "text/html; charset=UTF-8");
                multipart.addBodyPart(body);
                // 添加所有附件（添加时判断文件是否存在）
                if (CollectionUtils.isNotEmpty(attachFileList)){
                    for(String filePath : attachFileList){
                        if(Files.exists(Paths.get(filePath))){
                            MimeBodyPart tempBodyPart = new MimeBodyPart();
                            tempBodyPart.attachFile(filePath);
                            multipart.addBodyPart(tempBodyPart);
                        }
                    }
                }
                mimeMessage.setContent(multipart);
                // 开始发信
                mimeMessage.saveChanges();
                Transport.send(mimeMessage);
            }catch (Exception e) {
                System.out.println("发送邮件错误："+e.getMessage());
            }
        }
    }


    /**
     * 认证信息
     */
    static class MyAuthenticator extends Authenticator {

        /**
         * 用户名
         */
        String username;

        /**
         * 密码
         */
        String password;

        /**
         * 构造器
         *
         * @param username 用户名
         * @param password 密码
         */
        public MyAuthenticator(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }
}
