package com.companyName.reports;


import com.companyName.utils.CommonUtils;
import com.companyName.utils.GetFrameworkKeys;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;

public class EmailManager {

    private static final String EMAIL_ID = "EMAIL";
    private static final String PASSWORD = "appPasscode";
    public static String filename;

    /**
     * SendMail to ID given in configuration.properties with zipped reports
     * @param testName
     * @throws MessagingException
     */
    public static void sendEmail(String testName) throws MessagingException {

        System.out.println("Send Email Start");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.user", EMAIL_ID);
        props.put("mail.password", PASSWORD);

        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_ID, PASSWORD);
            }
        };

        Session session = Session.getInstance(props, auth);
        MimeMessage msg = new MimeMessage(session);
        msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
        msg.setFrom(new InternetAddress(EMAIL_ID));
        msg.setSubject("Subject " + testName + " Run Report " + new Date());
        BodyPart bodyPart = new MimeBodyPart();

        bodyPart.setText("Please Find The Attached Report File!");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(bodyPart);
        bodyPart = new MimeBodyPart();

        FileDataSource source = new FileDataSource(filename);
        bodyPart.setDataHandler(new DataHandler(source));
        bodyPart.setFileName("Report"+".zip");
        multipart.addBodyPart(bodyPart);
        msg.setContent(multipart);

        String emailTo = GetFrameworkKeys.getPropValue("EmailTO");
        String emailCC = GetFrameworkKeys.getPropValue("EmailCC");
        String emailBCC = GetFrameworkKeys.getPropValue("EmailBCC");

        if(!emailTo.isEmpty()) {
            msg.addRecipients(Message.RecipientType.TO, addEmails(emailTo));
        }
        if(!emailCC.isEmpty()) {
            msg.addRecipients(Message.RecipientType.CC, addEmails(emailCC));
        }
        if(!emailBCC.isEmpty()) {
            msg.addRecipients(Message.RecipientType.BCC, addEmails(emailBCC));
        }


        Transport.send(msg, msg.getAllRecipients());

        CommonUtils.logInfo("Email Sent");
    }

    private static InternetAddress[] addEmails(String emails) throws AddressException {
        CommonUtils.logInfo(Integer.toString(emails.length()));
        String[] recipientList = emails.split(",");
        InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
        int counter = 0;
        for (String recipient : recipientList) {
            recipientAddress[counter] = new InternetAddress(recipient.trim());
            counter++;
        }
        return  recipientAddress;
    }


}
