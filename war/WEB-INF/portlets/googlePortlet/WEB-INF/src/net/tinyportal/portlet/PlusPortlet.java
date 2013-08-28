package net.tinyportal.portlet;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import net.tinyportal.Constant;
import net.tinyportal.service.google.GoogleService;
import net.tinyportal.service.google.api.Gapi;
import net.tinyportal.service.google.plus.PlusService;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.Comment;
import com.google.api.services.plus.model.PeopleFeed;


public class PlusPortlet extends GenericPortlet {

	/**
	 * Logger 
	 */
	protected static Logger logger = Logger.getLogger("logger");
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1860670862928669876L;

	public void doView(RenderRequest request,RenderResponse response) 
			throws PortletException,IOException { 
		try {
			Gapi gapi = (Gapi) request.getAttribute(Constant.portlet_gapi);
			// USER
			Credential credential = (Credential)request.getAttribute(Constant.portlet_credentiel);
			GoogleService service = gapi.getService(PlusService.NAME);
			Plus plus = (Plus)service.getService(credential);
			
			logger.log(Level.INFO, "Recuperation cercles");
			PeopleFeed peoples = plus.people().list("me", "visible").execute();
			peoples.getItems();
			
			List<Activity> activities = plus.activities().list("106450527005571387719", "public").execute().getItems();
			List<Comment> comments = plus.comments().list(activities.get(0).getId()).execute().getItems();
			
			request.setAttribute("net.tinyportal.portlet.plus.activities", activities);
			request.setAttribute("net.tinyportal.portlet.plus.comments", comments);
			PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher("/WEB-INF/jsp/portlets/plus/view.jsp");
			dispatcher.include(request, response);
			
		} catch (Exception e) {
			response.getWriter().print(e.getMessage());
			throw new PortletException(e);
		}
	}
	
	  public void processAction (ActionRequest request, ActionResponse response) 
			    throws PortletException, java.io.IOException {
		  request.getParameter("action");
	  }
	  
}

