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
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

public class HtmlPresentationPortlet extends GenericPortlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1101904574258893267L;
	
	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(HtmlPresentationPortlet.class);

	public void doView(RenderRequest request,RenderResponse response) throws PortletException,IOException {
		PortletPreferences preferences = request.getPreferences();
		String content = preferences.getValue("content","Use edit mode to put text");
		response.setContentType("text/html");
		response.getPortletOutputStream().write(content.getBytes());

	}

	public void doEdit(RenderRequest request,RenderResponse response) throws PortletException,IOException { 
		PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher("/WEB-INF/jsp/portlets/htmlpresentation/edit.jsp");
		dispatcher.include(request, response);
	}

	public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException
	{
		PortletPreferences preferences = request.getPreferences();

		String content = request.getParameter("content");
		String title = request.getParameter("title");
		if (content != null) 		
			preferences.setValue("content", content);
		if (title != null && title.length()>0) 
			preferences.setValue("title", title);
		preferences.store();

	}

	protected String getTitle(RenderRequest request) {
		PortletPreferences preferences = request.getPreferences();
		String title = preferences.getValue("title", null);
		if (title != null) 
			return title;
		else
			return super.getTitle(request);
	}
}
