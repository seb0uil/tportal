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

package net.tinyportal.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

import net.tinyportal.Constant;
import net.tinyportal.javax.portlet.TpActionResponse;
import net.tinyportal.javax.portlet.TpPortletPreference;
import net.tinyportal.javax.portlet.TpPortletURL;
import net.tinyportal.tools.PortletXML;

/**
 * Bean représentant simplement le contenu d'un portlet.<br/>
 * Chaque instance de portlet est emballé par son propre PortletHolder.
 * Celui-ci permet de gérer le cycle de vie d'un portlet, ses changements d'états
 * ainsi que son titre et son contenu
 * 
 * @author seb0uil@gmail.com
 *
 */
public class PortletHolder implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2534239904725885136L;

	/**
	 * Titre du portlet
	 */
	private String Title = null;

	/**
	 * Contenu html du portlet
	 */
	private StringBuffer Content = new StringBuffer();

	/**
	 * Etat windowState du portlet
	 */
	private WindowState windowState = WindowState.NORMAL;

	/**
	 * Mode (VIEW, EDIT, HELP) du portlet
	 */
	private PortletMode portletMode = PortletMode.VIEW;


	private RenderRequest renderRequest;
	private RenderResponse renderResponse;
	private PortletConfig portletConfig;
	private TpActionResponse actionResponse;
	private TpPortletPreference preferences;

	/**
	 * Nom du portlet
	 */
	private String portletName;

	/**
	 * Id unique du portlet
	 */
	private String portletId;

	/**
	 * Configuration XML du portlet
	 */
	private PortletXML portletXML;


	public PortletXML getPortletXML() {
		return portletXML;
	}

	/**
	 * Portlet JSR géré par ce holder
	 */
	GenericPortlet portlet;

	/**
	 * Liste des types Mime supportés par le portlet
	 */
	private List<String> MimeType = new ArrayList<String>();

	
	public PortletHolder(PortletHolder bean) {
		PortletXML portletXML = bean.portletXML;

		TpPortletPreference preferences = new TpPortletPreference(portletXML.getPortletName(), portletXML.getPortletPreference(), portletXML.getReadOnlyPreferences());

		this.preferences = preferences;
		this.Title = new String();
		if (bean.Title != null) this.Title = new String(bean.Title);
		this.portletName = new String(bean.portletName);
		this.portletXML = bean.portletXML;
		this.MimeType = bean.MimeType;

		this.portlet = bean.getPortlet();
		portletId = TpPortletURL.getPortletId(this);
	}

	public PortletHolder(GenericPortlet portlet, PortletXML portletXML) {
		TpPortletPreference preferences = new TpPortletPreference(portletXML.getPortletName(), portletXML.getPortletPreference(), portletXML.getReadOnlyPreferences());
		this.preferences = preferences;
		this.Title = portletXML.getPortletTitle();
		this.portletName = portletXML.getPortletName();
		this.portletXML = portletXML;
		this.MimeType = portletXML.getMimeType();

		portletId = TpPortletURL.getPortletId(this);
		this.portlet = portlet;
	}

	/*
	 * Gestion de l'état fenêtré du portlet
	 */

	/**
	 * Retourne l'état du portlet
	 */
	public WindowState getWindowState() {
		return windowState;
	}

	/**
	 * Positionne un état pour l'instance du portlet
	 * @param windowState Etat à positionner
	 * @throws WindowStateException Dans le cas ou l'état n'est pas autorisé (par
	 * le portlet ou le portail) une PortletModeException est levée
	 */
	public void setWindowState(WindowState windowState) throws WindowStateException {
		if (!isWindowStateAllowed(windowState))
			throw new WindowStateException(windowState + " not allowed",windowState);
		if (!Constant.portal_context.isWindowStateAllowed(windowState))
			throw new WindowStateException(windowState + " not allowed by portal",windowState);
		this.windowState = windowState;
	}

	/**
	 * Détermine si l'état passé en paramètre est autorisé pour le portlet
	 * @param état à tester
	 * @return vrai si l'état est autorisé
	 */
	public boolean isWindowStateAllowed(String state) {
		return isWindowStateAllowed(new WindowState(state));
	}

	/**
	 * Détermine si l'état passé en paramètre est autorisé pour le portlet
	 * @param état à tester
	 * @return vrai si l'état est autorisé
	 */
	public boolean isWindowStateAllowed(WindowState state) {
		List<WindowState> windowStates = portletXML.getWindowsStates();
		return windowStates.contains(state);
	}

	/*
	 * Gestion du mode du portlet
	 */

	/**
	 * Retourne le mode en cours pour l'instance du portlet
	 */
	public PortletMode getPortletMode() {
		return portletMode;
	}

	/**
	 * Positionne un mode (VIEW, EDIT, HELP) pour l'instance du portlet
	 * @param portletMode mode à positionner
	 * @throws PortletModeException Dans le cas ou le mode n'est pas autorisé (par
	 * le portlet ou le portail) une PortletModeException est levée
	 */
	public void setPortletMode(PortletMode portletMode) throws PortletModeException {
		if (!isPortletModeAllowed(portletMode))
			throw new PortletModeException(portletMode + " not allowed",portletMode);
		if (!Constant.portal_context.isPortletModeAllowed(portletMode))
			throw new PortletModeException(portletMode + " not allowed by portal",portletMode);
		this.portletMode = portletMode;
	}

	/**
	 * Détermine si le mode passé en paramètre est autorisé pour le portlet
	 * @param mode à tester
	 * @return vrai si le mode est autorisé
	 */
	public boolean isPortletModeAllowed(String mode) {
		return isPortletModeAllowed(new PortletMode(mode));
	}

	/**
	 * Détermine si le mode passé en paramètre est autorisé pour le portlet
	 * @param mode à tester
	 * @return vrai si le mode est autorisé
	 */
	public boolean isPortletModeAllowed(PortletMode mode) {
		List<PortletMode> portletMode = portletXML.getPortletModes();
		return portletMode.contains(mode);	
	}

	/*
	 * Getter && Setter
	 */
	public RenderRequest getRenderRequest() {
		return renderRequest;
	}

	public void setRenderRequest(RenderRequest renderRequest) {
		this.renderRequest = renderRequest;
	}

	public RenderResponse getRenderResponse() {
		return renderResponse;
	}

	public void setRenderResponse(RenderResponse renderResponse) {
		this.renderResponse = renderResponse;
	}

	public PortletConfig getPortletConfig() {
		return portletConfig;
	}

	public void setPortletConfig(PortletConfig portletConfig) {
		this.portletConfig = portletConfig;
	}

	public String getPortletName() {
		return portletName;
	}

	public String getPortletId() {
		return portletId;
	}

	public void setPortletId(String portletId) {
		this.portletId = portletId;
	}

	public TpActionResponse getActionResponse() {
		return actionResponse;
	}

	public void setActionResponse(TpActionResponse actionResponse) {
		this.actionResponse = actionResponse;
	}

	public List<String> getMimeType() {
		return MimeType;
	}

	public void setMimeType(List<String> mimeType) {
		MimeType = mimeType;
	}

	public GenericPortlet getPortlet() {
		return portlet;
	}

	public void setPortlet(GenericPortlet portlet) {
		this.portlet = portlet;
	}

	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getContent() {
		return Content.toString();
	}
	public void setContent(String content) {
		Content = new StringBuffer(content);
	}

	public void addContent(String content) {
		Content.append(content);
	}

	public PortletPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(TpPortletPreference preferences) {
		this.preferences = preferences;
	}

	/**
	 * 
	 */
  	public PortletHolder clone() {
  		PortletHolder o = null;
    	try {
      		// On récupère l'instance à renvoyer par l'appel de la 
      		// méthode super.clone()
      		o = (PortletHolder) super.clone();
      		o.setContent(this.getContent());
      		
      		TpPortletPreference preferences = new TpPortletPreference(portletXML.getPortletName(), portletXML.getPortletPreference(), portletXML.getReadOnlyPreferences());
      		o.setPreferences(preferences);
      		
      		o.setPortletId(TpPortletURL.getPortletId(o));
      		try {
      			GenericPortlet portlet = getPortlet().getClass().newInstance().getClass().newInstance();
      			portlet.init(getPortlet().getPortletConfig());
				o.setPortlet(portlet);
				 
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PortletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
      		
    	} catch(CloneNotSupportedException cnse) {
      		// Ne devrait jamais arriver car nous implémentons 
      		// l'interface Cloneable
      		cnse.printStackTrace(System.err);
	    }
	    // on renvoie le clone
	    return o;
  	}
}
