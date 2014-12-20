package com.example.Moving;

import android.location.Address;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;

/**
 * Created by tiny on 12/20/14.
 */
public class MailSender {
    private PopupAuthenticator auth=new PopupAuthenticator();
    private Properties props=new Properties();
    MailSender(){
        props.put("mail.smtp.host","smtp.163.com");
        props.put("mail.auth","true");
    }
}

class PopupAuthenticator extends Authenticator{
    public PasswordAuthentication getPasswordAuthenication(){
        String userName="tinygamefeedback";
        String pwd="wytiny";
        char[] password=pwd.toCharArray();
        return new PasswordAuthentication(userName,password);
    }
}
