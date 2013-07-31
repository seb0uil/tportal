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

package net.tinyportal.portlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import net.portal.google.service.api.Gapi;
import net.tinyportal.Constant;

import org.apache.log4j.Logger;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;

public class LoginPortlet extends GenericPortlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2808827035812265075L;
	private static final Logger logger = Logger.getLogger(LoginPortlet.class);

	public void processAction (ActionRequest request, ActionResponse response) 
			throws PortletException, java.io.IOException {
		logger.error("Action");
	}

	public void doView(RenderRequest request,RenderResponse response) 
			throws PortletException,IOException { 
		Gapi gapi = (Gapi) request.getAttribute(Constant.portlet_gapi);
		// USER
		Credential credential = (Credential)request.getAttribute(Constant.portlet_credentiel);
		Oauth2 userService = gapi.getOauth2Service(credential);
		try {
			Userinfo userInfo = userService.userinfo().get().execute();
			request.setAttribute(Constant.portlet_properties_prefix+ "gapi.user.info", userInfo);

			if (userInfo == null) {
				PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher("/WEB-INF/jsp/portlets/login/login.jsp");
				dispatcher.include(request, response);
			} else {
				PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher("/WEB-INF/jsp/portlets/login/logged.jsp");
				dispatcher.include(request, response);			
			}
		} catch (GoogleJsonResponseException e) {
			if (e.getStatusCode() == 401) {
				// The user has revoked our token or it is otherwise bad.
				// Delete the local copy so that their next page load will recover.
			}
		}
	}
}
