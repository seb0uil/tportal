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

package portlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

public	 class HelloWorldPortlet extends GenericPortlet implements PortletConfig { 
	private static final Logger logger = Logger.getLogger(HelloWorldPortlet.class);
	{
		logger.warn("dispatcher ok :");
	}


	private Integer cpt = 0;
	
	public void doView(RenderRequest request,RenderResponse response) 
	throws PortletException,IOException { 

		final StringBuffer lStringBuffer= new StringBuffer();
		request.getUserPrincipal();
		request.isUserInRole("user");
		// Get our preferences
		PortletPreferences pref = request.getPreferences();

		// Get the value of "displaytext" from our preferences, if not available,
		// then use the second string passed to the function
		String displayText = pref.getValue("displaytext", "MISSING: display-text");
		// displays the string from our preferences

		response.setContentType(request.getResponseContentType()); 

		String param = request.getParameter("param");
		request.setAttribute("myAttr", new String("Mon attribut"));
//		PortletRequestDispatcher dispatcher =
//			getPortletContext().getRequestDispatcher("/WEB-INF/hello.jsp");
//		dispatcher.include(request, response);
//
//		response.setContentType("text/html");
		
//		OutputStream os = response.getPortletOutputStream();
//		os.write(Character.getNumericValue('c'));
//		os.flush();
		
		PrintWriter writer = response.getWriter(); 
		writer.write(displayText);
		writer.write(new Integer(cpt++).toString());
		
		PortletURL url = response.createActionURL();
		writer.write("<br/> <a href='"+url+"'>" + url+ "</a>");
		writer.flush();
	} 
	
	public void processAction (ActionRequest request, ActionResponse response) 
		    throws PortletException, java.io.IOException {
		cpt=0;
	}
}

