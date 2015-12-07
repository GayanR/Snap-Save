package email;

import android.os.Environment;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;



/**
 * Created by Nuwan on 11/7/2015.
 */
public class GMailSender extends javax.mail.Authenticator{

    //Place to store the reports
    public static final String DATA_PATH_REPORTS = Environment
            .getExternalStorageDirectory().toString() + "/SnapAndSave/reports/";

    private String mailhost = "smtp.gmail.com";
    private Session session;
    private Properties props;
    static {
        Security.addProvider(new JSSEProvider());
    }

    public GMailSender() {


        props= new Properties();
        //props.setProperty("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        // props.put("mail.smtp.socketFactory.fallback", "false");
        //props.setProperty("mail.user",user);
        //props.setProperty("mail.password",password);
        //props.setProperty("mail.smtp.quitwait", "false");


    }



    public synchronized void sendMail(String subject, String body, String sender, String recipients, String filename) throws Exception {

        session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("snapnsave.sliit@gmail.com", "sliit123..");
            }
        });
        MimeMessage message = new MimeMessage(session);
        //DataHandler handler = new DataHandler(new FileDataSource(Environment.getExternalStorageDirectory().toString() + "/SnapAndSave/reports/20151028_002459.pdf"));
        message.setFrom(new InternetAddress(sender));
        message.setSubject(subject);
        BodyPart messageBodyPart1=new MimeBodyPart();
        messageBodyPart1.setText(body);

        MimeBodyPart messageBodyPart2 =new MimeBodyPart();
        //messageBodyPart2.setDataHandler(handler);
        messageBodyPart2.attachFile(DATA_PATH_REPORTS + filename + ".pdf");
        //messageBodyPart2.setFileName("20151028_002459.pdf");

        Multipart multipart =new MimeMultipart();
        multipart.addBodyPart(messageBodyPart1);
        multipart.addBodyPart(messageBodyPart2);
        message.setContent(multipart);
        //message.setContent(body,"text/html; charset=utf-8");
        //message.setDataHandler(handler);
        if (recipients.indexOf(',') > 0)
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
        else
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
        Transport.send(message);

    }


}
