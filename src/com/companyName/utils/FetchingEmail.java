package com.companyName.utils;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.AndTerm;
import javax.mail.search.RecipientTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class FetchingEmail {

    int count =0;
    boolean timeVariant;
    private Message[] messages;
    public FetchingEmail(boolean timeVariant){
        this.timeVariant = timeVariant;
    }

    /**
     * To fetch mail from gmail inbox.
     * EmailUser: mailID
     * EmailPassword: App password
     * @param Subject
     * @param Recipient
     * @param prefix
     * @param suffix
     * @return
     */
    public String fetchRequiredTextFromMail(String Subject, String Recipient, String prefix, String suffix, String userName, String password) {

        while (count < 6) {
        try {
            String host = "imap.gmail.com";// change accordingly
            String protocol = "imaps";
            String file = null;
            CommonUtils.threadWait(10);
            URLName url = new URLName(protocol, host, 993, file, userName, password);
            Session session = null;
            MailSSLSocketFactory socketFactory = new MailSSLSocketFactory();
            socketFactory.setTrustAllHosts(true);
            if (session == null) {
                Properties props = null;
                try {
                    props = System.getProperties();
   					props.setProperty("mail.imap.ssl.enable", "true");
                } catch (SecurityException se) {
                    props = new Properties();
                }
                session = Session.getInstance(props);
            }
            Store store = session.getStore(url);
            store.connect(host, userName, password);
            Folder emailFolder = store.getFolder("Inbox");
            emailFolder.open(Folder.READ_WRITE);
            SearchTerm term = new AndTerm(new SubjectTerm(Subject), new RecipientTerm(Message.RecipientType.TO, new InternetAddress(Recipient)));
            Message[] messages = null;
            messages = emailFolder.search(term);
            Message message = messages[messages.length - 1];
            boolean isRead = message.getFlags().contains(Flags.Flag.ANSWERED);
            CommonUtils.logInfo("Is Email Read : "+ isRead);
            long d1 = new Date().getTime();
            long d2 = message.getReceivedDate().getTime();
            long diff = d1 - d2;
            double difference_In_Minutes = diff / 60000;
            if(!isRead){
                if(timeVariant) {
                    if (difference_In_Minutes < 5) {
                        CommonUtils.logInfo("Mail subject text " + message.getSubject());
                        message.setFlag(Flags.Flag.ANSWERED,true);
                        return getBetweenStrings(getText(message).replaceAll("[\n\t\r ]", ""), prefix.replaceAll("[\n\t\r ]", ""), suffix.replaceAll("[\n\t\r ]", "")).trim();
                    }
                }else{
                    CommonUtils.logInfo("Mail subject text " + message.getSubject());
                    message.setFlag(Flags.Flag.ANSWERED,true);
                    return getBetweenStrings(getText(message).replaceAll("[\n\t\r ]", ""), prefix.replaceAll("[\n\t\r ]", ""), suffix.replaceAll("[\n\t\r ]", "")).trim();
                }
            }else {
                CommonUtils.logInfo("No unread Emails");
            }
            emailFolder.close(true);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
            count++;
    }

        return null;

    }

    public String getBetweenStrings(String text, String textFrom, String textTo) {
        String result = text.substring(text.indexOf(textFrom) + textFrom.length());
        result = result.substring(0,result.indexOf(textTo));
        return result;
    }

    public String getText(Part p) throws MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
            return s;
        }
        if (p.isMimeType("multipart/alternative")) {
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }
        return null;
    }
}
