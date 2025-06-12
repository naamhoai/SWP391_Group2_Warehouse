package controller;

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ResetServiceServlet {
    private final int LIMIT_MINUTES = 10;
    static final String FROM = "ezminh216@gmail.com";
    static final String PASSWORD = "bqaw bzvt weej pxpn";

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public LocalDateTime expireDateTime() {
        return LocalDateTime.now().plusMinutes(LIMIT_MINUTES);
    }

    public boolean isExpired(LocalDateTime time) {
        return LocalDateTime.now().isAfter(time);
    }

    public boolean sendEmail(String to, String link, String name) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PASSWORD);
            }
        };

        Session session = Session.getInstance(props, auth);

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(FROM));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject("Reset Password", "UTF-8");
            String content = "<h1>Hello " + name + "</h1><p>Click the link below to reset your password:<br><a href='" + link + "'>Reset Password</a></p>";
            msg.setContent(content, "text/html; charset=UTF-8");
            Transport.send(msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}  