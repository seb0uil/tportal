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

import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletSecurityException;
import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import net.tinyportal.Constant;
import net.tinyportal.bean.PortletHolder;

public class ActionURL extends TagSupport {

	String portletMode = null;
	public String getPortletMode() {
		return portletMode;
	}
	public void setPortletMode(String portletMode) {
		this.portletMode = portletMode;
	}

	String windowState = null;
	public String getWindowState() {
		return windowState;
	}
	public void setWindowState(String windowState) {
		this.windowState = windowState;
	}
	
	String secure = null;
	public String getSecure() {
		return secure;
	}
	public void setSecure(String secure) {
		this.secure = secure;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6737363641150734388L;
	
	private PortletURL portletURL=null;

	public int doStartTag() throws JspException {

		portletURL = getPortletURL();
		try {
			if (portletMode!=null)
				portletURL.setPortletMode(new PortletMode(portletMode));
		} catch (PortletModeException pme) { }
		
		try {
			if (windowState!=null)
				portletURL.setWindowState(new WindowState(windowState));
		} catch (WindowStateException wse) { }
		
		try {
			if (secure!=null)
				portletURL.setSecure(new Boolean(secure));
		} catch (PortletSecurityException pse) { }	
		return Tag.EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag() throws JspException {
		try {

			/**
			 * On affiche dans la page le titre du portlet
			 */
			pageContext.getOut().print ( portletURL.toString());
			return EVAL_PAGE; 
			
			
		} catch (Exception e) {
			throw new JspException ("Error", e); 
		} 
	}
	
	protected PortletURL getPortletURL() {
		PortletHolder portletBean = (PortletHolder) pageContext.getAttribute(Constant.session_portlet_bean);
		if (portletBean == null)
			portletBean = (PortletHolder)pageContext.getRequest().getAttribute(Constant.session_portlet_bean);

		PortletURL portletURL = ((RenderResponse)pageContext.getAttribute("renderResponse")).createActionURL();
		return portletURL;
	}
	
	protected void addParam(String key, String value) {
		portletURL.setParameter(key, value);
	}

}
