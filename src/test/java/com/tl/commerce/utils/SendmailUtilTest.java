package com.tl.commerce.utils;

import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;

class SendmailUtilTest {

    @Test
    void sendEmail() {
        try {

            SendmailUtil.sendEmil465("3119894079@qq.com","测试发送邮件stmp","【美美搭】美搭网验证码:379694（20分钟内有效，如非本人操作，请忽略）");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}