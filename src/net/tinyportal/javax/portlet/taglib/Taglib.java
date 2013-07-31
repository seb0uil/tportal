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

package net.tinyportal.javax.portlet.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import net.tinyportal.Constant;

public class Taglib extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2661746885797398291L;

	
	
	public int doStartTag() throws JspException { 		
		pageContext.setAttribute("renderRequest", 
				(pageContext.getRequest().getAttribute(Constant.portlet_request)));
		
		pageContext.setAttribute("renderResponse", 
				(pageContext.getRequest().getAttribute(Constant.portlet_response)));		
		pageContext.setAttribute("portletConfig", 
				(pageContext.getRequest().getAttribute(Constant.portlet_config)));
		return Tag.SKIP_BODY;
	}
}
