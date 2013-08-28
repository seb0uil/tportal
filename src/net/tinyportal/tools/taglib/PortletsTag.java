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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * La classe PortletsTag permet, par le taglib associé, de parcourir la liste
 * des portlets a afficher dans une page et les placer à tour de rôle
 * dans le contexte de la page. Ceux-ci peuvent alors être récupérer et
 * traiter dans la jsp.
 * @author seb0uil@gmail.com
 *
 */
public class PortletsTag extends TagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String portletName = null;

	public int doStartTag() throws JspException { 
		try {
			pageContext.include("/portlet?portlet="+portletName);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
		return Tag.SKIP_BODY; 
	} 

	public int doAfterBody() throws JspException {
			return Tag.SKIP_BODY; 
	}

	public int doEndTag() throws JspException {
		return Tag.EVAL_PAGE;
	}

	public void setName(String portletName) {
		this.portletName = portletName;
	}
}
