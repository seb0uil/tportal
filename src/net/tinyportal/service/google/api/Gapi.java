/*
    This file is part of tPortal.

    tPortal is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    tPortal is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with tPortal.  If not, see <http://www.gnu.org/licenses/>.

    The original code was written by Sebastien Bettinger <sebastien.bettinger@gmail.com>

 */

package net.tinyportal.service.google.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.tinyportal.service.google.GoogleService;
import net.tinyportal.service.google.credential.CredentialManager;
import net.tinyportal.service.google.drive.DriveService;
import net.tinyportal.service.google.plus.PlusService;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;

public class Gapi implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3670522300156179624L;

	/**
	 * Default JSON factory for Google Apis Java client.
	 */
	protected final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	/**
	 * Path component under war/ to locate client_secrets.json file.
	 */
	public final String CLIENT_SECRETS_FILE_PATH = (Boolean.parseBoolean(System.getProperty("local","false")))?"/WEB-INF/client_secrets_local.json":"/WEB-INF/client_secrets.json";
	
	/**
	 * Key to get/set userId from and to the session.
	 */
	public final String KEY_SESSION_USERID = "user_id";

	/**
	 * Default transportation layer for Google Apis Java client.
	 */
	protected final HttpTransport TRANSPORT = new NetHttpTransport();
	
	private CredentialManager credentialManager = null;

	
	protected static List<GoogleService> services = new ArrayList<GoogleService>();
	static {
		services.add(new PlusService());
		services.add(new DriveService());
//		services.add(new CalendarService());
	}
	
	/**
	 * Reads client_secrets.json and creates a GoogleClientSecrets object.
	 * @return A GoogleClientsSecrets object.
	 */
	private GoogleClientSecrets getClientSecrets(ServletContext servletContext) {
		// TODO: do not read on each request
		InputStream stream =
				servletContext.getResourceAsStream(CLIENT_SECRETS_FILE_PATH);
		try {
			return GoogleClientSecrets.load(JSON_FACTORY, stream);
		} catch (IOException e) {
			throw new RuntimeException("No client_secrets.json found");
		}
	}
	
	public Gapi(ServletContext servletContext) throws ServletException {
		List<String> scopes = new ArrayList<String>();
		scopes.add("https://www.googleapis.com/auth/userinfo.email");
		scopes.add("https://www.googleapis.com/auth/userinfo.profile");
		scopes.add("https://www.googleapis.com/auth/drive");
		for (GoogleService service : services) {
			scopes.addAll(service.getScopes());
			service.setHttpTransport(TRANSPORT);
			service.setJsonFactory(JSON_FACTORY);
		}
		// init credential manager
		credentialManager = new CredentialManager(
				getClientSecrets(servletContext), TRANSPORT, JSON_FACTORY);
		credentialManager.setScope(scopes);
	}
	
	/**
	 * Redirects to OAuth2 consent page if user is not logged in.
	 * @param req   Request object.
	 * @param resp  Response object.
	 */
	public boolean loginIfRequired(HttpServletRequest req, HttpServletResponse resp) {
		Credential credential = getCredential(req, resp);
		if (credential == null) {
			// redirect to authorization url
			try {
				resp.sendRedirect(credentialManager.getAuthorizationUrl());
				return true;
			} catch (IOException e) {
				throw new RuntimeException("Can't redirect to auth page");
			}
		}
		return false;
	}
	
	/**
	 * Returns the credentials of the user in the session. If user is not in the
	 * session, returns null.
	 * @param req   Request object.
	 * @param resp  Response object.
	 * @return      Credential object of the user in session or null.
	 */
	public Credential getCredential(HttpServletRequest req,
			HttpServletResponse resp) {
		String userId = (String) req.getSession().getAttribute(KEY_SESSION_USERID);
		if (userId != null) {
			return credentialManager.get(userId);
		} 
		return null;
	}

	/**
	 * Deletes the credentials of the user in the session permanently and removes
	 * the user from the session.
	 * @param req   Request object.
	 * @param resp  Response object.
	 */
	public void deleteCredential(HttpServletRequest req,
			HttpServletResponse resp) {
		String userId = (String) req.getSession().getAttribute(KEY_SESSION_USERID);
		if (userId != null) {
			credentialManager.delete(userId);
			req.getSession().removeAttribute(KEY_SESSION_USERID);
		}
	}
	
	/**
	 * Build and return an Oauth2 service object based on given request parameters.
	 * @param credential User credentials.
	 * @return Drive service object that is ready to make requests, or null if
	 *         there was a problem.
	 */
	public Oauth2 getOauth2Service(Credential credential) {
		return new Oauth2.Builder(TRANSPORT, JSON_FACTORY, credential).build();
	}
	
	public boolean handleCallbackIfRequired(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		String code = req.getParameter("code");
		if (code != null) {
			// retrieve new credentials with code
			Credential credential = credentialManager.retrieve(code);
			// request userinfo
			Oauth2 service = getOauth2Service(credential);
			try {
				Userinfo about = service.userinfo().get().execute();
				String id = about.getId();
				credentialManager.save(id, credential);
				req.getSession().setAttribute(KEY_SESSION_USERID, id);
			} catch (IOException e) {
				throw new RuntimeException("Can't handle the OAuth2 callback, " + 
						"make sure that code is valid.");
			}
			resp.sendRedirect("/");
			return true;
		}
		return false;
	}
	
	/* Gestion des services */
	public GoogleService getService (String Name) {
		for (GoogleService service : services) {
			if (service.getName().equalsIgnoreCase(Name)) {
				return service;
			}
		}
		return null;
	}
}
