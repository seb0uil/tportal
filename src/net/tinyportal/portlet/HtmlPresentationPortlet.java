package net.tinyportal.portlet;

import java.io.IOException;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

public class HtmlPresentationPortlet extends GenericPortlet {
	private static final Logger logger = Logger.getLogger(HtmlPresentationPortlet.class);
	
	public void doView(RenderRequest request,RenderResponse response) throws PortletException,IOException { 
		PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher("/WEB-INF/jsp/portlets/htmlpresentation/index.jsp");
		dispatcher.include(request, response);
	}
	
}
