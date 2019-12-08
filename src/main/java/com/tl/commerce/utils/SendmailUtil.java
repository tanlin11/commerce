package com.tl.commerce.utils;
 
import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Date;
import java.util.Properties;

/**
 * 注册邮箱验证码，忘记密码验证码
 */
public class SendmailUtil {

    /**
     * QQ邮箱的 SMTP 服务器地址
     */
    private static final String myEmailSMTPHost = "smtp.qq.com";

    /**
     * 发件人
     */
    private static final String myEmailAccount = "320740231@qq.com";

    /**
     * 授权码
     */
    private static final String  myEmailPassword = "glleiwndxgcecaae";
    /**
     * 端口
     */
    private static final String port="25";
    /**
     * 开启debug调试，发送服务器需要验证身份
     */
    private static final String flag="true";
    /**
     * 启用SSL认证
     */
    private static final String isSSL="true";
    /**
     * 发送邮件使用协议
     */
    private static final String protocol="smtp";

    private static final Properties props = new Properties();
    static{
        props.setProperty("mail.debug", flag);
        props.setProperty("mail.smtp.auth", flag);
        props.setProperty("mail.smtp.port", port);
        props.setProperty("mail.smtp.host", myEmailSMTPHost);
        props.setProperty("mail.transport.protocol", protocol);
        props.setProperty("mail.smtp.starttls.enable", flag);
    }
    
    /**
     * 邮件单发（自由编辑短信，并发送，适用于私信）
     *
     * @param toEmailAddress 收件箱地址
     * @param emailTitle 邮件主题
     * @param emailContent 邮件内容
     * @throws Exception
     */
    public static void sendEmail(String toEmailAddress, String emailTitle, String emailContent)
            throws  MessagingException, UnsupportedEncodingException {

        //创建会话
        Session session = Session.getInstance(props);
         
        //获取邮件对象
        Message msg = new MimeMessage(session);
         
        //设置邮件标题
        msg.setSubject(emailTitle);
         
        //设置邮件内容
        StringBuilder builder = new StringBuilder();
         
        //写入内容
        builder.append("\n" + emailContent);

        //设置显示的发件时间
        msg.setSentDate(new Date());
         
        //设置邮件内容
        msg.setText(builder.toString());
         
        //设置发件人邮箱
        msg.setFrom(new InternetAddress(myEmailAccount,"美美搭登陆验证", "UTF-8"));

        Transport transport = session.getTransport();

        //连接邮箱账户
        transport.connect( myEmailSMTPHost, myEmailAccount, myEmailPassword);
         
        //发送邮件
        transport.sendMessage(msg, new Address[] { new InternetAddress(toEmailAddress) });
        System.out.println("end.....");
        transport.close();
    }


    public static void sendEmil465(String to, String emailTitle,String emailContent) throws Exception {

            final String smtpHost="smtp.qq.com";
            final String smtpPort="465";
            final String username = "320740231@qq.com";
            final String password = "glleiwndxgcecaae";


            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

            //设置邮件会话参数
            Properties props = new Properties();
            //邮箱的发送服务器地址
            props.setProperty("mail.smtp.host", smtpHost);
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.debug", flag);
            //SSL认证，注意腾讯邮箱是基于SSL加密的，所有需要开启才可以使用，很多人不成功是因为漏写了下面的代码
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.socketFactory", sf);
            props.setProperty("mail.smtp.ssl.enable", "true");

            //邮箱发送服务器端口,这里设置为465端口
            props.setProperty("mail.smtp.port", smtpPort);
            props.setProperty("mail.smtp.socketFactory.port", smtpPort);
            props.put("mail.smtp.auth", "true");

            //获取到邮箱会话,利用匿名内部类的方式,将发送者邮箱用户名和密码授权给jvm
            Session session = Session.getDefaultInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            session.setDebug(true);
            //通过会话,得到一个邮件,用于发送
            Message msg = new MimeMessage(session);
            //设置邮件标题
            msg.setSubject(emailTitle);
            //设置发件人
            msg.setFrom(new InternetAddress(username));
            //设置收件人,to为收件人,cc为抄送,bcc为密送
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            //msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(to, false));
            //msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(to, false));
            //设置邮件消息
            msg.setText(emailContent);
            //设置发送的日期
            msg.setSentDate(new Date());

            //调用Transport的send方法去发送邮件
            Transport.send(msg);



    }
}