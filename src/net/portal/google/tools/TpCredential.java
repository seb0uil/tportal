package net.portal.google.tools;

import java.io.Serializable;

import com.google.api.client.auth.oauth2.Credential;

public class TpCredential extends Credential implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 388832275106247559L;

	String token = null;
	public TpCredential(Credential credential) {
		super(credential.getMethod());
		token = credential.getAccessToken();
	}
	
	
	
	
}
