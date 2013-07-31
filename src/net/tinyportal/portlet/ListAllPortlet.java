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
import java.util.HashSet;
import java.util.Set;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import net.tinyportal.Portal;

import org.apache.log4j.Logger;

public class ListAllPortlet extends GenericPortlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5176701510146773320L;
	private static final Logger logger = Logger.getLogger(ListAllPortlet.class);

	public void doView(RenderRequest request,RenderResponse response) 
			throws PortletException,IOException { 
		Set<String> portlets = new HashSet(Portal.getPortletSet());
 
		request.setAttribute("portlets", portlets);
		PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher("/WEB-INF/jsp/portlets/listAll/view.jsp");
		dispatcher.include(request, response);
	}
}
