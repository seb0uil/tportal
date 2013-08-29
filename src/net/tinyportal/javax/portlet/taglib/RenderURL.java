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

import javax.portlet.PortletURL;

import net.tinyportal.Constant;
import net.tinyportal.bean.PortletHolder;

public class RenderURL extends ActionURL {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8600379354900750028L;

	protected PortletURL getPortletURL() {
		PortletHolder portletBean = (PortletHolder) pageContext.getAttribute(Constant.session_portlet_bean);
		if (portletBean == null)
			portletBean = (PortletHolder)pageContext.getRequest().getAttribute(Constant.session_portlet_bean);

		PortletURL portletURL = portletBean.getRenderResponse().createRenderURL();
		return portletURL;
	}
	
}
