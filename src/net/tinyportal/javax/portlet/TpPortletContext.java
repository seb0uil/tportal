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

package net.tinyportal.javax.portlet;

import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequestDispatcher;
import javax.servlet.ServletContext;

import net.tinyportal.tools.PortletXML;

public class TpPortletContext implements PortletContext, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5909480183383429780L;
	transient ServletContext context;
	PortletXML portletXml;
	
	Properties attribute = new Properties();
	Properties initParameter = new Properties();
	
	public TpPortletContext(ServletContext context, PortletXML portletXml) {
		this.context = context;
		this.portletXml = portletXml;
	}

	public String getServerInfo() {
		return context.getServerInfo();
	}

	public PortletRequestDispatcher getRequestDispatcher(String path) {

		return new TpPortletRequestDispatcher(path);
	}

	public PortletRequestDispatcher getNamedDispatcher(String name) {
		return null; //new TpPortletRequestDispatcher(context.getNamedDispatcher(name));
	}

	public InputStream getResourceAsStream(String path) {
		return context.getResourceAsStream(portletXml.getPortletPath()+"/"+path);
		
	}

	public int getMajorVersion() {
		return 1;
	}

	public int getMinorVersion() {
		return 0;
	}

	public String getMimeType(String file) {
		return context.getMimeType(portletXml.getPortletPath()+"/"+file);
	}

	public String getRealPath(String path) {
		return context.getRealPath(portletXml.getPortletPath()+"/"+path);
	}

	public Set getResourcePaths(String path) {
		return context.getResourcePaths(portletXml.getPortletPath()+"/"+path);
	}

	public URL getResource(String path) throws MalformedURLException {
		return context.getResource(portletXml.getPortletPath()+"/"+path);
	}

	public Object getAttribute(String name) {
		return (String)attribute.getProperty(name);
	}

	public Enumeration getAttributeNames() {
		return attribute.elements();
	}

	public String getInitParameter(String name) {
		return (String)initParameter.getProperty(name);
	}

	public Enumeration getInitParameterNames() {
		return initParameter.elements();
	}

	public void log(String msg) {
		context.log(msg);		
	}

	public void log(String message, Throwable throwable) {
		context.log(message, throwable);
	}

	public void removeAttribute(String name) {
		attribute.remove(name);
	}

	public void setAttribute(String name, Object object) {
		attribute.put(name, object);
	}
	
	public TpPortletContext clone() {
		return new TpPortletContext(context, portletXml);
	}

	@Override
	public String getPortletContextName() {
		return portletXml.getPortletName();
	}

	public PortletXML getPortletXml() {
		return portletXml;
	}

}
