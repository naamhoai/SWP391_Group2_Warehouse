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
            msg.setSubject("Đặt lại mật khẩu", "UTF-8");
            String content = "<h2>Xin chào " + name + ",</h2>"
                + "<p>Bạn hoặc ai đó vừa yêu cầu đặt lại mật khẩu cho tài khoản của bạn.</p>"
                + "<p>Vui lòng nhấn vào liên kết bên dưới để đặt lại mật khẩu mới:</p>"
                + "<p><a href='" + link + "'>Đặt lại mật khẩu</a></p>"
                + "<p>Nếu bạn không yêu cầu, vui lòng bỏ qua email này.</p>"
                + "<p>Trân trọng,<br>Hệ thống quản lý vật tư</p>";
            msg.setContent(content, "text/html; charset=UTF-8");
            Transport.send(msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendPasswordEmail(String to, String name, String password) {
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
            msg.setSubject("Mật khẩu mới của bạn", "UTF-8");
            String content = "<h2>Xin chào " + name + ",</h2>"
                + "<p>Mật khẩu mới của bạn là: <b>" + password + "</b></p>"
                + "<p>Bạn có thể sử dụng mật khẩu này để đăng nhập vào hệ thống.</p>"
                + "<p>Trân trọng,<br>Hệ thống quản lý vật tư</p>";
            msg.setContent(content, "text/html; charset=UTF-8");
            Transport.send(msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
} 