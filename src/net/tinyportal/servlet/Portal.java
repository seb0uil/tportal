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

package net.tinyportal.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.portal.google.service.api.Gapi;
import net.tinyportal.Constant;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;

public class Portal extends HttpServlet  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7934374005770615573L;
	
	/**
	 * Jsp du portail, définie dans le web.xml
	 */
	private String portalJsp;

	private Gapi gapi;
	
	/**
	 * A l'initialisation de la servlet, on va initialiser les différents portlets présent<br/>
	 * <br/>
	 * Pour cela, on va parcourir le répertoire des portlets, les initialiser un a un
	 */
	public void init(ServletConfig config) {
		try {
			super.init(config);
			/*
			 * On va charger les portlets disponibles
			 */
			ServletContext context = getServletContext();

			gapi = new Gapi(context);
			/*
			 * On charge la valeur de la jsp
			 */
			portalJsp = context.getInitParameter("JSPPortal");
			
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
		    // handle OAuth2 callback
			boolean isRedirect = gapi.handleCallbackIfRequired(request, response);
			if (isRedirect) return;
			
		    // Making sure that we have user credentials
		    isRedirect = gapi.loginIfRequired(request, response);
		    if (isRedirect) return;
		    request.setAttribute(Constant.portlet_gapi, gapi);
		    
		    Credential credential = gapi.getCredential(request, response);
		    request.setAttribute(Constant.portlet_credentiel, credential);
//		    request.setAttribute(Constant.portlet_properties_prefix+ "response", response);
		    
				
			Oauth2 userService = gapi.getOauth2Service(credential);
			try {
				Userinfo userInfo = userService.userinfo().get().execute();
				request.setAttribute(Constant.portlet_properties_prefix+ "gapi.user.info", userInfo.getEmail());


			} catch (GoogleJsonResponseException e) {
				if (e.getStatusCode() == 401) {
					// The user has revoked our token or it is otherwise bad.
					// Delete the local copy so that their next page load will recover.
					gapi.deleteCredential(request, response);
				}
			}
		    
			response.setHeader("Content-Type", "text/html");
			ServletContext context = getServletContext();
			RequestDispatcher servletDispatcher = context.getRequestDispatcher(portalJsp);
			
			
			servletDispatcher.include(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	
}
