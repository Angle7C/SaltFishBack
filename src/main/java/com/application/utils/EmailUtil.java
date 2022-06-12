package com.application.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;

@Component
@Data
@Slf4j
public class EmailUtil {
      private static  final Logger logger=LoggerFactory.getLogger("邮箱记录");
    private  static String emailCode;

    @Value(value="${pass}")
    private String code;
    @PostConstruct
    public void init(){
        emailCode=code;
    }
    public static String setEmailCode(String Email,String Title){
        String code = RandomUtil.randomString(6);
        logger.info("发送地址：{} 验证码：{}",Email,code);
        MailAccount account=new MailAccount();
        account.setHost("smtp.163.com");
        account.setPort(25);
        account.setAuth(true);
        account.setFrom("15305067103@163.com");
        account.setUser("15305067103@163.com");
        account.setPass(emailCode);
        String send = MailUtil.send(account, Email, Title, code, false);
        logger.info("返回结果：{}",send);
        return code;
    }
}
