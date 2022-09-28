package com.blog.service;


import java.util.List;

public interface SendEmailService {
    /**
     * 发送邮件
     * @param fromAliasName 别名
     * @param to 发送目标
     * @param subject 主题
     * @param content 内容
     * @param attachFileList 附件
     */
    void send(String fromAliasName,String to,String subject,String content, List<String> attachFileList);
}
