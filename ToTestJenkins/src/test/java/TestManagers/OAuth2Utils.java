package TestManagers;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.sheets.v4.SheetsScopes;

import Utilities.FileUtilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class OAuth2Utils extends DriverManager{
	
	protected JsonFactory JSON_FACTORY;
	private String TOKENS_DIRECTORY_PATH;
	private List<String> SCOPES;
	private String CREDENTIALS_FILE_PATH;
	private String GOOGLE_USER_AUTH;
	private String GOOGLE_ACCESS_TYPE;
	private int port;

	protected OAuth2Utils()
	{
		this.JSON_FACTORY = JacksonFactory.getDefaultInstance();
		this.TOKENS_DIRECTORY_PATH = getConfig("TOKENS_DIRECTORY_PATH");
		this.SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS, GmailScopes.MAIL_GOOGLE_COM);
		this.CREDENTIALS_FILE_PATH = FileUtilities.abs(getConfig("CREDENTIALS_FILE_PATH"));
		this.port = Integer.valueOf(getConfig("googlePort"));
		this.GOOGLE_USER_AUTH=getConfig("GOOGLE_USER_AUTH");
		this.GOOGLE_ACCESS_TYPE=getConfig("GOOGLE_ACCESS_TYPE");
	}

	/**
	 * Creates an authorized Credential object.
	 * @param HTTP_TRANSPORT The network HTTP Transport.
	 * @return An authorized Credential object.
	 * @throws IOException If the credentials.json file cannot be found.
	 */
	protected Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT){
		Credential auth = null;
		// Load Parameters.
		//File initialFile = new File("src/main/resources/sample.txt");
		try {
		File initialFile = new File(CREDENTIALS_FILE_PATH);
		InputStream in = new FileInputStream(initialFile);

		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
				.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
				.setAccessType(GOOGLE_ACCESS_TYPE)
				.build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(port).build();
		auth = new AuthorizationCodeInstalledApp(flow, receiver).authorize(GOOGLE_USER_AUTH);
		}
		catch(Exception e)
		{
			System.out.println("Error Occurred during Google OAuth 2.0 initializing credential.");
		}
		return auth;
	}
}