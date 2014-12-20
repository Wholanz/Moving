package com.example.Moving;

import android.util.Log;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by tiny on 12/20/14.
 */
public class GameMailSender {

    private final static String LOG_TAG="Game Mail Sender";
    private final static String SUBJECT="Game Feedback";
    private final static String MAILBOX="tinygamefeedback@163.com";
    private final static String MAILUSER="tinygamefeedback";
    private final static String PASSWORD="wytiny";
    private final static String SENDTO="zjutiny@gmail.com";

    public static void Send (String name,String text) throws AddressException, MessagingException {
            Log.d(LOG_TAG,"Sending Mail");
            Properties properties = new Properties();
            properties.setProperty("mail.transport.protocol", "smtp");
            properties.setProperty("mail.smtp.auth", "true");
            Session session = Session.getInstance(properties);
            session.setDebug(true);
            Message messgae = new MimeMessage(session);
            messgae.setFrom(new InternetAddress(MAILBOX));

            messgae.setText(text+"\nBy "+name);
            messgae.setSubject(SUBJECT);

            Transport tran = session.getTransport();
            tran.connect("smtp.163.com", 25, MAILBOX, PASSWORD);
            tran.sendMessage(messgae, new Address[]{ new InternetAddress(SENDTO)});
            tran.close();
    }

}


