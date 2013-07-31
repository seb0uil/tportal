package net.portal.google.service.drive;

import java.util.Arrays;
import java.util.List;

import net.portal.google.service.GoogleService;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.drive.Drive;

public class DriveService extends GoogleService {

	static String[] SCOPE =  {"https://www.googleapis.com/auth/drive"};
	static public String NAME = "DRIVE";

	@Override
	public List<String> getScopes() {
		return Arrays.asList(SCOPE);
	}

	/**
	 * Build and return a Google+ service object based on given request parameters.
	 * @param credential User credentials.
	 * @return Google+ service object that is ready to make requests, or null if
	 *         there was a problem.
	 */
	public Drive getService(Credential credential) {
		return new Drive.Builder(TRANSPORT, JSON_FACTORY, credential).build();
	}

	@Override
	public String getName() {
		return NAME;
	}
}
