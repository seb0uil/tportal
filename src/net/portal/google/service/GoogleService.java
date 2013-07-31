package net.portal.google.service;

import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

public abstract class GoogleService {

	/**
	 * Default transportation layer for Google Apis Java client.
	 */
	protected static HttpTransport TRANSPORT = null;

	/**
	 * Default JSON factory for Google Apis Java client.
	 */
	protected static JsonFactory JSON_FACTORY = null;

	abstract public List<String> getScopes();
	abstract public String getName();

	/**
	 * Build and return a Google+ service object based on given request parameters.
	 * @param credential User credentials.
	 * @return Google+ service object that is ready to make requests, or null if
	 *         there was a problem.
	 */
	abstract public AbstractGoogleJsonClient getService(Credential credential);

	public void setHttpTransport(HttpTransport httpTransport) {
		TRANSPORT = httpTransport;
	}

	public void setJsonFactory(JsonFactory jsonFactory) {
		JSON_FACTORY = jsonFactory;		
	}
	
}
