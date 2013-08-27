package net.portal.google.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.gson.Gson;

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
	
	abstract public void service(HttpServletRequest request, HttpServletResponse response);

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
	
	/**
	 * Dumps the given object as JSON and responds with given HTTP status code.
	 * @param resp  Response object.
	 * @param code  HTTP status code to respond with.
	 * @param obj   An object to be dumped as JSON.
	 */
	protected void sendJson(HttpServletResponse resp, int code, Object obj) {
		try {
			// TODO(burcud): Initialize Gson instance for once.
			resp.setContentType("application/json");
			resp.getWriter().print(new Gson().toJson(obj).toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Dumps the given object to JSON and responds with HTTP 200.
	 * @param resp  Response object.
	 * @param obj   An object to be dumped as JSON.
	 */
	protected void sendJson(HttpServletResponse resp, Object obj) {
		sendJson(resp, 200, obj);
	}
	
}
