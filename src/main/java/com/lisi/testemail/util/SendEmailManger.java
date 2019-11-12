package com.lisi.testemail.util;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 *
 * 邮箱登录
 */

public class SendEmailManger extends Thread{
    private String mailAdr;
    private String content;
    private String subject;

    /**
     * 构造函数
     * @param mailAdr  邮箱地址
     * @param subject   邮箱题目
     * @param content   邮箱内容
     */

    public SendEmailManger(String mailAdr, String subject, String content) {
        super();
        this.mailAdr = mailAdr;
        this.subject = subject;
        this.content = content;
    }

    /**
     * 多线程，一份新可以发给多个用户
     */
    @Override
    public void run() {
        super.run();
        try {
            sendMail(mailAdr, subject, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //写信
    private void sendMail(String mailAdr, String subject, String content) throws Exception {
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        final Properties props = new Properties();
        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.qq.com");       //邮箱协议
        // smtp登陆的账号、密码 ；需开启smtp登陆
        props.setProperty("mail.debug", "true");
        props.put("mail.user", "1786678583@qq.com");       //需要改，你开通的QQ邮箱地址
        props.put("mail.password", "你开通qq邮箱授权码");   //需要改，你开通qq邮箱授权码
        // 特别需要注意，要将ssl协议设置为true,否则会报530错误
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);   //不用填
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人
        try {
            InternetAddress form = new InternetAddress(props.getProperty("mail.user"));
            message.setFrom(form);
            // 设置收件人
            InternetAddress to = new InternetAddress(mailAdr);
            message.setRecipient(Message.RecipientType.TO, to);
            // 设置抄送
            // InternetAddress cc = new InternetAddress("591566764@qq.com");
            // message.setRecipient(RecipientType.CC, cc);
            // 设置密送，其他的收件人不能看到密送的邮件地址
            // InternetAddress bcc = new InternetAddress("mashen@163.com");
            // message.setRecipient(RecipientType.CC, bcc);
            // 设置邮件标题
            message.setSubject(subject);
            // 设置邮件的内容体
            message.setContent(content, "text/html;charset=UTF-8");
            // 发送邮件
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
//        SendEmailManger d = new SendEmailManger("邮件接收者地址", "码神联盟", "尊敬的用户你好： <br/><br/><a href='https://mp.weixin.qq.com/s/BJR5vfgWIyEDN88PU3g6xA' style='color:red;'>微信公众号码神联盟真不错....</a>");
        int code =(int) (Math.random()*10000);

        SendEmailManger d = new SendEmailManger("1272763772@qq.com", "李思", "尊敬的用户你好：你的验证码为"+code);
        d.start();
    }
}
