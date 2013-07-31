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

package net.tinyportal.tools.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import net.tinyportal.Constant;
import net.tinyportal.bean.PortletHolder;

public class PortletBeanTitleTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int doStartTag() throws JspException { 
		try {

			/**
			 * On affiche dans la page le titre du portlet
			 */
			pageContext.getOut().println (
					((PortletHolder) pageContext.getAttribute(Constant.session_portlet_bean)).getTitle()
			);
			return Tag.SKIP_BODY; 
			
			
		} catch (Exception e) {
			throw new JspException ("Error", e); 
		} 

	} 
	
}
