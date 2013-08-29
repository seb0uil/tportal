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
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class TpHttpServletRequest extends HttpServletRequestWrapper implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1759777063952127274L;

	public TpHttpServletRequest(HttpServletRequest request) {
		super(request);
	}

	@Override
	public Enumeration<String> getAttributeNames(){
		return new TpEnumeration<>(new ArrayList());
	}
	
	@Override
	public void removeAttribute(String name) {
		System.out.println("Remove Name :: " + name);
//		super.removeAttribute(name);
	}
	
	@Override
	public void setAttribute(String name, Object o) {
		System.out.println("Set Name :: " + name);
//		super.setAttribute(name, o);
	}
}

