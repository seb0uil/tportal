package net.tinyportal.portlet;

import java.io.IOException;
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
import net.tinyportal.service.google.drive.DriveService;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;

public class DrivePortlet extends GenericPortlet {

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
			GoogleService service = gapi.getService(DriveService.NAME);
			Drive drive = (Drive)service.getService(credential);
			
			logger.log(Level.INFO, "Recuperation des fichiers !!!!");
			String type = request.getParameter("type");
			if (!"application/vnd.google-apps.folder".equals(type) && type != null)
				return;
			
			String folder = request.getParameter("id");
			folder = (folder==null)?"root":folder;
//			List<File> result = new ArrayList<File>();
			Files.List fileList = drive.files().list().setMaxResults(10) //.setFields("items(id, mimeType,title)")
					.setQ("'"+folder+"' in parents");
//			do {
//				try {
//					FileList files = fileList.execute();
//					for (File f : files.getItems()) {
//						System.out.println(f.getTitle() + " - " + f.getMimeType() + " - " + f.getId());
//						
//					}
//					
//					result.addAll(files.getItems());
//					fileList.setPageToken(files.getNextPageToken());
//				} catch (IOException e) {
//					fileList.setPageToken(null);
//				}
//			} while (fileList.getPageToken() != null &&
//					fileList.getPageToken().length() > 0);
//			
			request.setAttribute("net.tinyportal.portlet.drive", fileList);
			
			PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher("/WEB-INF/jsp/portlets/drive/view.jsp");
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
