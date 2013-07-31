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
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;

import net.tinyportal.Constant;
import net.tinyportal.tools.TpResourceBundle;

public class TpPortletConfig implements PortletConfig , Cloneable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5611131656140182331L;

	TpPortletContext porletContext;
	
	/**
	 * Paramètres positionnés dans le portlet.xml
	 * dans le noeud <init-param>
	 */
	Map<String, String> initParameter;
	
	/**
	 * Nom du portlet tq définit dans le portlet.xml
	 */
	String portletName;
	
	/**
	 * ClassLoader utilisé pour le portlet
	 */
	ClassLoader loader;
	
	/**
	 * Nom du bundle de message utilisé par le portlet
	 */
	String bundleBaseName;
	
	public TpPortletConfig(TpPortletContext porletContext, ClassLoader loader)
	{
		this.porletContext = porletContext;
		this.loader = loader;
		
		this.portletName = porletContext.getPortletXml().getPortletName();
		this.initParameter = porletContext.getPortletXml().getInitParameters();
		
		/*
		 * Si le portlet ne déclare pas de bundle, on utilise celui du portail
		 */
		this.bundleBaseName = porletContext.getPortletXml().getBundle();
		if (this.bundleBaseName == null) this.bundleBaseName = Constant.portal_context.getProperty(Constant.portal_bundle);
	}

	@Override
	public String getPortletName() {
		return portletName;
	}

	@Override
	public PortletContext getPortletContext() {
		return this.porletContext;
	}

	@Override
	public ResourceBundle getResourceBundle(Locale locale) {
		ResourceBundle messages = ResourceBundle.getBundle(bundleBaseName, locale, loader);
		return  new TpResourceBundle(messages, porletContext.getPortletXml().getPortletInfo());
	}

	@Override
	public String getInitParameter(String name) {
		if (name == null) throw new IllegalArgumentException();
		return (String) this.initParameter.get(name);
	}

	@Override
	public Enumeration getInitParameterNames() {
		return Collections.enumeration(this.initParameter.keySet());
	}
	
	/*
	 * On a besoin de pouvoir cloner la config afin de gérer des config de portlet différente
	 * pour chaque session.
	 * @see java.lang.Object#clone()
	 */
	public TpPortletConfig clone() {
		return new TpPortletConfig(porletContext.clone(), loader);
	}

}
