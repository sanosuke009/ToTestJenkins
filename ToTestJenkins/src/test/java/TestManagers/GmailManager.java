package TestManagers;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Draft;
import com.google.api.services.gmail.model.Message;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import Utilities.FileUtilities;

public class GmailManager extends OAuth2Utils{
	

	private final String APPLICATION_NAME = "Gmail API Utilities";
	protected GmailManager() {
		//super();
		// TODO Auto-generated constructor stub
	}

	
    /**
     * Create a MimeMessage using the parameters provided.
     *
     * @param to email address of the receiver
     * @param from email address of the sender, the mailbox account
     * @param subject subject of the email
     * @param bodyText body text of the email
     * @return the MimeMessage to be used to send email
     * @throws MessagingException
     */
    public MimeMessage createEmail(String to,
                                          String from,
                                          String subject,
                                          String bodyText){
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        try {
			email.setFrom(new InternetAddress(from));
			email.addRecipient(javax.mail.Message.RecipientType.TO,
			        new InternetAddress(to));
			email.setSubject(subject);
			email.setText(bodyText);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return email;
    }
    
    /**
     * Create a message from an email.
     *
     * @param emailContent Email to be set to raw of message
     * @return a message containing a base64url encoded email
     * @throws IOException
     * @throws MessagingException
     */
    public Message createMessageWithEmail(MimeMessage emailContent){
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        Message message = new Message();
        try {
			emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        message.setRaw(encodedEmail);
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return message;
    }
	
	
	/**
     * Create draft email.
     *
     * @param service an authorized Gmail API instance
     * @param userId user's email address. The special value "me"
     * can be used to indicate the authenticated user
     * @param emailContent the MimeMessage used as email within the draft
     * @return the created draft
     * @throws MessagingException
     * @throws IOException
     */
    public Draft createDraft(Gmail service,
                                    String userId,
                                    MimeMessage emailContent){
    	
        Message message = createMessageWithEmail(emailContent);
        Draft draft = new Draft();
        try {
        draft.setMessage(message);
        draft = service.users().drafts().create(userId, draft).execute();

        System.out.println("Draft id: " + draft.getId());
        System.out.println(draft.toPrettyString());
        }
        catch (Exception e) {
			// TODO: handle exception
		}
        return draft;
    }
    
    /**
     * Create a MimeMessage using the parameters provided.
     *
     * @param to Email address of the receiver.
     * @param from Email address of the sender, the mailbox account.
     * @param subject Subject of the email.
     * @param bodyText Body text of the email.
     * @param file Path to the file to be attached.
     * @return MimeMessage to be used to send email.
     * @throws MessagingException
     */
    public static MimeMessage createEmailWithAttachment(String to,
                                                        String from,
                                                        String subject,
                                                        String bodyText,
                                                        File file){
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        try {
			email.setFrom(new InternetAddress(from));
			email.addRecipient(javax.mail.Message.RecipientType.TO,
			        new InternetAddress(to));
			email.setSubject(subject);

			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(bodyText, "text/plain");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);

			mimeBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(file);

			mimeBodyPart.setDataHandler(new DataHandler(source));
			mimeBodyPart.setFileName(file.getName());

			multipart.addBodyPart(mimeBodyPart);
			email.setContent(multipart);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return email;
    }
    
    /**
     * Send an email from the user's mailbox to its recipient.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me"
     * can be used to indicate the authenticated user.
     * @param emailContent Email to be sent.
     * @return The sent message
     * @throws MessagingException
     * @throws IOException
     */
    public Message sendMessage(Gmail service,
                                      String userId,
                                      MimeMessage emailContent) {
        Message message = createMessageWithEmail(emailContent);
        try {
			message = service.users().messages().send(userId, message).execute();
		

        System.out.println("Message id: " + message.getId());
        System.out.println(message.toPrettyString());
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return message;
    }

    public void sendReport()
    {
    	try {
			// Build a new authorized API client service.
			NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
			        .setApplicationName(APPLICATION_NAME)
			        .build();
			String to="sanosuke009@gmail.com";
			String from="roysano999@gmail.com";
			String subject="Email Through API";
			String bodyText="Hey Posh intel!!!How did the English Breakfast in the afternoon feel?";
			String userId="me";
			File attach = new File(FileUtilities.abs("Results/cucumber-report-html_12090528/cucumber-html-reports/overview-features.html"));
			
			//MimeMessage emailContent = createEmail(to, from, subject, bodyText);
			MimeMessage emailContentAttachment = createEmailWithAttachment(to, from, subject, bodyText, attach);
			Message message = sendMessage(service, userId, emailContentAttachment);
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}