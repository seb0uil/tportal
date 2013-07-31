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

package net.tinyportal.tools;

import java.io.Serializable;

import javax.portlet.PortletMode;

public class TpPortletSessionConfig implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7042599971460613191L;
	private PortletMode portletMode = PortletMode.VIEW;

	public void setPortletMode(PortletMode portletMode) {
		this.portletMode = portletMode;
	}

	public PortletMode getPortletMode() {
		return portletMode;
	}
}
