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

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;

import net.tinyportal.Constant;
import net.tinyportal.tools.TpEnumeration;

public class TpPortletRequest implements PortletRequest, Serializable {

//	PortletHolder portletHolder;

	/**
	 * 
	 */
	private static final long serialVersionUID = 112363794025584044L;

	TpPortletSession portletSession = null;

	TpPortletContext tpPortletContext;

	transient HttpServletRequest httpRequest;
	
	HashMap<String, Object> attributes = new HashMap<String, Object>();
	
	
	private HashMap<String, String[]> parameters = new HashMap<String, String[]>();

	public TpPortletRequest(String portletId, HttpServletRequest httpRequest, TpPortletContext tpPortletContext) {
//		this.portletHolder = portletBean;
		this.tpPortletContext = tpPortletContext;
		this.httpRequest = httpRequest;
		
		/*
		 * A partir des paramètres positionnés pour la requete http, on récupère les paramètres
		 * liés au portlet.
		 * Pour cela, on retire le préfix [portletId]_param_ lorsqu'il est présent
		 */
		Enumeration parameterEnum = httpRequest.getParameterNames();
		StringBuffer sbParam = new StringBuffer(portletId).append("_param_");
		while (parameterEnum.hasMoreElements()) {
			String parameters = (String) parameterEnum.nextElement();
			if (parameters.startsWith(sbParam.toString())) {
				String value = httpRequest.getParameter(parameters);
				String parameter = parameters.substring(sbParam.length());
				this.parameters.put(parameter, new String[] {value});
			} else {
				String value = httpRequest.getParameter(parameters);
				this.parameters.put(parameters, new String[] {value});
			}
		}
		
		parameterEnum = httpRequest.getAttributeNames();
		sbParam = new StringBuffer(portletId).append("_param_");
		while (parameterEnum.hasMoreElements()) {
			String parameters = (String) parameterEnum.nextElement();
			if (parameters.startsWith(sbParam.toString())) {
				Object value = httpRequest.getAttribute(parameters);
				String parameter = parameters.substring(sbParam.length());
				this.attributes.put(Constant.portlet_properties_prefix + parameter, value);
			} else {
				Object value = httpRequest.getAttribute(parameters);
				this.attributes.put(Constant.portlet_properties_prefix + parameters, value);
			}
		}
	}

	@Override
	public boolean isWindowStateAllowed(WindowState state) {
		return tpPortletContext.getPortletXml().getWindowsStates().contains(state);
	}

	@Override
	public boolean isPortletModeAllowed(PortletMode mode) {
		return tpPortletContext.getPortletXml().getPortletModes().contains(mode);
	}

	@Override
	public PortletMode getPortletMode() {
		return (PortletMode)this.httpRequest.getAttribute("net.tinyportal.portletMode");
	}

	@Override
	public WindowState getWindowState() {
		return (WindowState)this.httpRequest.getAttribute("net.tinyportal.windowState");
	}

	@Override
	public PortletPreferences getPreferences() {
		return (PortletPreferences)this.httpRequest.getAttribute("net.tinyportal.portletPreference");

	}

	@Override
	public PortletSession getPortletSession() {
		return getPortletSession(true);
	}

	@Override
	public PortletSession getPortletSession(boolean create) {
		if (!create)
			return this.portletSession;
		else 
			if (this.portletSession == null || !this.portletSession.isValidSession()) 
				this.portletSession = new TpPortletSession(this.httpRequest.getSession(true), this.tpPortletContext);
		return this.portletSession;
	}

	@Override
	public String getProperty(String name) {
		Enumeration<String> enumeration = getProperties(name);
		if (enumeration.hasMoreElements())
			return enumeration.nextElement();
		else
			return null;
		
	}

	/**
	 * On va concaténer différentes valeurs afin de construire l'énumération
	 * de retour.<br/>
	 * On regarde donc: </br>
	 * <ul>
	 * 	<li>Dans les entêtes http</li>
	 *  <li>Dans le contextes du portail</li>
	 *  <li>Dans les paramètres de la requête http</li>
	 * </ul>
	 */
	@Override
	public Enumeration<String> getProperties(String name) {
		if (name == null) throw new IllegalArgumentException("Null value");
		
		List<String> l = new ArrayList<String>();
		
		/*
		 * On charge l'éventuel entête avec ce nom
		 */
		String property = this.httpRequest.getHeader(name);
		if (property != null) 
			l.add(property);
		
		Enumeration<String> properties = Constant.portal_context.getProperties(name);
		if (properties != null) 
			while (properties.hasMoreElements()) {
				l.add(properties.nextElement());
			}
		
		String[] propertiesArray = parameters.get(name);
		if (propertiesArray != null)
			l.addAll(Arrays.asList(propertiesArray));
		
		Enumeration<String> enumeration = new TpEnumeration<String>(l);
		return enumeration;
	}

	@Override
	public Enumeration getPropertyNames() {
		return Constant.portal_context.getPropertyNames();
	}

	@Override
	public PortalContext getPortalContext() {
		return Constant.portal_context;
	}

	@Override
	public String getAuthType() {
		return this.httpRequest.getAuthType();
	}

	@Override
	public String getContextPath() {
		String portletPath = tpPortletContext.getPortletXml().getPortletPath();
		if (portletPath.equals("ROOT"))
				return "";
		return "/"+portletPath;
	}

	@Override
	public String getRemoteUser() {
		return this.httpRequest.getRemoteUser();
	}

	@Override
	public Principal getUserPrincipal() {
		return this.httpRequest.getUserPrincipal();
	}

	/**
	 * Le rôle est récupéré après avoir éventuellement été
	 * mappé sur ceux géré dans la définition XML du portlet
	 */
	@Override
	public boolean isUserInRole(String role) {
		if (this.tpPortletContext.getPortletXml().getSecurity().containsKey(role))
			role =  this.tpPortletContext.getPortletXml().getSecurity().get(role);
		return this.httpRequest.isUserInRole(role);
	}

	@Override
	public Object getAttribute(String name) {
		return attributes.get(Constant.portlet_properties_prefix + name);
	}

	@Override
	public Enumeration getAttributeNames() {
		Set<String> set = new HashSet<String>();
		Iterator<String> it = attributes.keySet().iterator();
		while (it.hasNext()) {
			set.add(Constant.portlet_properties_prefix + it.next());
		}
		return Collections.enumeration(set);
	}
	
	@Override
	public String getParameter(String name) {
		if (this.parameters.containsKey(name))
			return this.parameters.get(name)[0];
		else
			return null;
	}

	@Override
	public Enumeration getParameterNames() {
		return Collections.enumeration(this.parameters.keySet());
	}

	@Override
	public String[] getParameterValues(String name) {
			return this.parameters.get(name);
	}

	@Override
	public Map getParameterMap() {
		return this.parameters;
	}

	@Override
	public boolean isSecure() {
		return this.httpRequest.isSecure();
	}

	@Override
	public void setAttribute(String name, Object o) {
		attributes.put(Constant.portlet_properties_prefix + name, o);
	}

	@Override
	public void removeAttribute(String name) {
		attributes.remove(Constant.portlet_properties_prefix + name);
	}

	@Override
	public String getRequestedSessionId() {
		return this.httpRequest.getRequestedSessionId();
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		return this.httpRequest.isRequestedSessionIdValid();
	}

	@Override
	public String getResponseContentType() {
		while (getResponseContentTypes().hasMoreElements()) {
			return (String) getResponseContentTypes().nextElement();
		}
		return null;
	}

	@Override
	public Enumeration getResponseContentTypes() {
		
		return Collections.enumeration(this.tpPortletContext.portletXml.getMimeType());
	}

	@Override
	public Locale getLocale() {
		return this.httpRequest.getLocale();
	}

	@Override
	public Enumeration getLocales() {
		return this.httpRequest.getLocales();
	}

	@Override
	public String getScheme() {
		return this.httpRequest.getScheme();
	}

	@Override
	public String getServerName() {
		return this.httpRequest.getServerName();
	}

	@Override
	public int getServerPort() {
		return this.httpRequest.getServerPort();
	}
	
	/*
	 * Getter & Setter
	 */
	public void setParameter(String name, String value) {
		this.parameters.put(name, new String[] {value});
	}
	
	public void setParameters(String name, String[] values) {
		this.parameters.put(name, values);
	}

}
