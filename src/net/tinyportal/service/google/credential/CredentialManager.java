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

package net.tinyportal.service.google.credential;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.auth.oauth2.AppEngineCredentialStore;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

/**
 * Credential manager to get, save, delete user credentials.
 *
 * @author jbd@google.com (Burcu Dogan)
 */
public class CredentialManager {

  /**
   * Client secrets object.
   */
  private GoogleClientSecrets clientSecrets;

  /**
   * Transport layer for OAuth2 client.
   */
  private HttpTransport transport;

  /**
   * JSON factory for OAuth2 client.
   */
  private JsonFactory jsonFactory;

  /**
   * Scopes for which to request access from the user.
   */
  public  List<String> SCOPES = Arrays.asList(
      "https://www.googleapis.com/auth/userinfo.email",
      "https://www.googleapis.com/auth/userinfo.profile"
      );

  /**
   * Credential store to get, save, delete user credentials.
   */
  private static AppEngineCredentialStore credentialStore =
      new AppEngineCredentialStore();

  /**
   * Credential Manager constructor.
   * @param clientSecrets App client secrets to be used during OAuth2 exchanges.
   * @param transport Transportation layer for OAuth2 client.
   * @param factory JSON factory for OAuth2 client.
   */
  public CredentialManager(GoogleClientSecrets clientSecrets,
      HttpTransport transport, JsonFactory factory) {
    this.clientSecrets = clientSecrets;
    this.transport = transport;
    this.jsonFactory = factory;
  }

  public void setScope(List<String> scope) {
	  SCOPES = scope;
  }
  /**
   * Builds an empty credential object.
   * @return An empty credential object.
   */
  public Credential buildEmpty() {
    return new GoogleCredential.Builder()
        .setClientSecrets(this.clientSecrets)
        .setTransport(transport)
        .setJsonFactory(jsonFactory)
        .build();
  }

  /**
   * Returns credentials of the given user, returns null if there are none.
   * @param userId The id of the user.
   * @return A credential object or null.
   */
  public Credential get(String userId) {
    Credential credential = buildEmpty();
    if (credentialStore.load(userId, credential)) {
      return credential;
    }
    return null;
  }

  /**
   * Saves credentials of the given user.
   * @param userId The id of the user.
   * @param credential A credential object to save.
   */
  public void save(String userId, Credential credential) {
    credentialStore.store(userId, credential);
  }

  /**
   * Deletes credentials of the given user.
   * @param userId The id of the user.
   */
  public void delete(String userId) {
    credentialStore.delete(userId, get(userId));
  }

  /**
   * Generates a consent page url.
   * @return A consent page url string for user redirection.
   */
  public String getAuthorizationUrl() {
    GoogleAuthorizationCodeRequestUrl urlBuilder =
        new GoogleAuthorizationCodeRequestUrl(
        clientSecrets.getWeb().getClientId(),
        clientSecrets.getWeb().getRedirectUris().get(0),
        SCOPES).setAccessType("offline"); //.setApprovalPrompt("force");
	  return urlBuilder.build();
  }

  /**
   * Retrieves a new access token by exchanging the given code with OAuth2
   * end-points.
   * @param code Exchange code.
   * @return A credential object.
   */
  public Credential retrieve(String code) {
    try {
      GoogleTokenResponse response = new GoogleAuthorizationCodeTokenRequest(
          transport,
          jsonFactory,
          clientSecrets.getWeb().getClientId(),
          clientSecrets.getWeb().getClientSecret(),
          code,
          clientSecrets.getWeb().getRedirectUris().get(0)).execute();
      return buildEmpty().setAccessToken(response.getAccessToken());
    } catch (IOException e) {
      new RuntimeException("An unknown problem occured while retrieving token");
    }
    return null;
  }
}
