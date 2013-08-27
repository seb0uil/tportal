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

public class Param extends TagSupport {
	String value = null;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	String name = null;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6737363641150734388L;

	public int doStartTag() throws JspException {


		if ( getParent() instanceof ActionURL ) {
			ActionURL parent = (ActionURL) getParent();
			parent.addParam(name, value);
		} else if ( getParent() instanceof RenderURL ) {
			ActionURL parent = (ActionURL) getParent();
			parent.addParam(name, value);
		}

		return Tag.EVAL_BODY_INCLUDE;
	}



}
