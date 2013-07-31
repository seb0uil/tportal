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

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

public class TaglibTEI extends TagExtraInfo {

	public VariableInfo[] getVariableInfo(TagData tagData) {
		return new VariableInfo[] {
			new VariableInfo("renderRequest", "javax.portlet.RenderRequest",true,VariableInfo.AT_END),
			new VariableInfo("renderResponse","javax.portlet.RenderResponse",true,VariableInfo.AT_END),
			new VariableInfo("portletConfig","javax.portlet.PortletConfig",true,VariableInfo.AT_END)
		};
	}
}
