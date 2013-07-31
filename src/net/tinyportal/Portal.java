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

package net.tinyportal;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.tinyportal.bean.PortletHolder;
import net.tinyportal.javax.portlet.TpActionRequest;
import net.tinyportal.javax.portlet.TpActionResponse;
import net.tinyportal.javax.portlet.TpPortletContext;
import net.tinyportal.javax.portlet.TpPortletRequest;
import net.tinyportal.javax.portlet.TpRenderRequest;
import net.tinyportal.javax.portlet.TpRenderResponse;
import net.tinyportal.tools.FictiveHttpServletResponse;

public class Portal implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 356999288626575514L;
	/*
	 * Gestion des portlets a ne pas prendre en compte
	 */
	private static List<String> onlyThesesPortlets = new ArrayList<String>();
	private static List<String> disabledPortlet = new ArrayList<String>();	
	public static List<String> getDisabledPortlet() {
		return disabledPortlet;
	}
	
	public static void addDisabledPortlet(String porletName) {
		disabledPortlet.add(porletName);
	}
	
	public static void addOnlyPortlet(String porletName) {
		onlyThesesPortlets.add(porletName);
	}
	
	public static void resetOnlyPortlet() {
		onlyThesesPortlets = new ArrayList<String>();
	}
	
	public static boolean isActivePortlet(String portletName) {
		if (!onlyThesesPortlets.isEmpty()) 
			return onlyThesesPortlets.contains(portletName);
		else
			return (!disabledPortlet.contains(portletName));
	}

	private static Map<String, PortletHolder> portletPool = new HashMap<String, PortletHolder>();
	
	public static void addPortlet(String portletName, PortletHolder portlet) {
		portletPool.put(portletName, portlet);
	}
	
	public static void addPortlets(Map<String, PortletHolder> portletsMap) {
		portletPool.putAll(portletsMap);
	}
	
	public static PortletHolder getPortlet(String portletName) {
		return portletPool.get(portletName);
	}
	
	public static Set<String> getPortletSet() {
		return portletPool.keySet();
	}
	
	/*
	 * On recupere soit le portlet de la session, soit un nouveau portlet qui est alors mis en session
	 */
	public static PortletHolder getPortlet(HttpServletRequest request, String portletName) {
		HttpSession session = request.getSession(true);
		PortletHolder bean = (PortletHolder)session.getAttribute(portletName);
		if (bean == null) {
			PortletHolder legacy = portletPool.get(portletName);
			session.setAttribute(portletName, bean = legacy.clone());
		}
		
		return bean;
	}
	
	
	/**
	 * Map stockant les contexts des portlets connus du portail
	 */
	private static Map<String, TpPortletContext> portletsContextPool = new HashMap<String, TpPortletContext>();
	
	/**
	 * Ajoute un context de portlet dans le conteneur
	 * @param contextPath
	 * @param context
	 */
	public static void addContext(TpPortletContext context) {
		portletsContextPool.put(context.getPortletXml().getPortletPath(), context);
	}
	public static TpPortletContext getContext(String contextPath) {
		return portletsContextPool.get(contextPath);
	}
	public static boolean isContextExist(String contextPath) {
		return portletsContextPool.containsKey(contextPath);
	}
	public static boolean isContextExist(TpPortletContext context) {
		return portletsContextPool.containsValue(context);
	}
	
	
	public static void doAction(HttpServletRequest request, HttpServletResponse response, PortletHolder portletHolder, GenericPortlet portlet) 
			throws WindowStateException, PortletModeException, PortletException, IOException {

		TpPortletContext TpPortletContext = (TpPortletContext) portlet.getPortletContext();
		TpActionRequest actionRequest = new TpActionRequest(portletHolder.getPortletId(),request,TpPortletContext);
		TpActionResponse actionResponse = new TpActionResponse(portletHolder,response);

		setWindowState(request, portletHolder);
		setPortletMode(request, portletHolder);
		setPortletParameters(request, portletHolder, actionRequest);
		
		try {
			portlet.processAction(actionRequest, actionResponse);
			if (actionResponse.isRedirected())
				response.sendRedirect(actionResponse.getRedirection());
		} catch (IllegalStateException ise) {
			actionResponse.cancelRedirected();
		}
		portletHolder.setActionResponse(actionResponse);

	}


	public static void doRefresh(HttpServletRequest request, HttpServletResponse response, PortletHolder portletHolder, GenericPortlet portlet) throws PortletException, IOException, ServletException {
		//Si une redirection est a faire, on ne fait pas de rendu
		if (portletHolder.getActionResponse()!=null && portletHolder.getActionResponse().isRedirected()) return;
		/*
		 * Calcul du portlet
		 */
		TpRenderRequest TPrequest = (TpRenderRequest)portletHolder.getRenderRequest();
		TpRenderResponse TPresponse = (TpRenderResponse)portletHolder.getRenderResponse();

//		RequestDispatcher servletDispatcher;
		portlet.render(TPrequest,TPresponse);

		String path = (String)TPrequest.getAttribute(Constant.portlet_dispatcher);
		if (path != null) {

			Enumeration eAttribute = TPrequest.getAttributeNames();
			while (eAttribute.hasMoreElements()) {
				String attribute = (String) eAttribute.nextElement();
				Object value = TPrequest.getAttribute(attribute);
				request.setAttribute(attribute, value);
			}
			
//			String path = tpDispatcher.getPath();
			path = (TPrequest.getContextPath()+path).substring(2);
			RequestDispatcher servletDispatcher = request.getRequestDispatcher(path);
			HttpServletResponse newResponse = new FictiveHttpServletResponse((HttpServletResponse) response);
//			Enumeration attrs = request.getAttributeNames();
//			String content = "<b>"+path+"</b><br/>";
//			while (attrs.hasMoreElements()) {
//				content += (String)attrs.nextElement() + "<br/>";
//			}
			

			servletDispatcher.include(request, newResponse);
			portletHolder.setContent(newResponse.toString());
//			portletHolder.setContent(content);
		} else {
			portletHolder.setContent("TEST");
		}

		try {
			if (portlet.getPortletConfig().getResourceBundle(request.getLocale()).containsKey("javax.portlet.title"))
				portletHolder.setTitle(TPresponse.getTitle());
		} catch (Exception e) {
			//
		}
	}

	public static void doRender(HttpServletRequest request, HttpServletResponse response, PortletHolder portletHolder, GenericPortlet portlet) {
		TpRenderRequest TPrequest = (TpRenderRequest)portletHolder.getRenderRequest();
		if (portletHolder.getActionResponse()!=null) 
			TPrequest.getParameterMap().putAll(portletHolder.getActionResponse().getRenderParametersMap());
		setWindowState(request, portletHolder);
		setPortletMode(request, portletHolder);
		setPortletParameters(request, portletHolder, TPrequest);
	}

	public static void setPortletParameters(HttpServletRequest httpRequest, PortletHolder portletHolder, TpPortletRequest portletRequest) {
		String portletId = portletHolder.getPortletId();
		StringBuffer sbParam = new StringBuffer(portletId).append("_param_");
		Enumeration<String> parameterEnum = httpRequest.getParameterNames();
		while (parameterEnum.hasMoreElements()) {
			String parameters = (String) parameterEnum.nextElement();
			if (parameters.startsWith(sbParam.toString())) {
				String value = httpRequest.getParameter(parameters);
				String parameter = parameters.substring(sbParam.length());
				portletRequest.setParameter(parameter, value);
			}							
		}
	}

	/**
	 * Positionne le mode du portlet en fonction
	 * des paramètres de la requête
	 * @param request
	 * @param portletHolder
	 */
	public static void setPortletMode(HttpServletRequest request,PortletHolder portletHolder) {
		String portletId = portletHolder.getPortletId();
		StringBuffer sb = new StringBuffer(portletId).append("_").append("mode");
		String mode = request.getParameter(sb.toString());
		if (mode != null) {
			try {
				PortletMode portletMode = new PortletMode(mode);
				portletHolder.setPortletMode(portletMode);
			} catch (PortletModeException pme) {
				pme.printStackTrace();
			}
		}
	}

	/**
	 * Positionne le mode du portlet en fonction
	 * des paramètres de la requête
	 * @param request
	 * @param portletHolder
	 */
	public static void setWindowState(HttpServletRequest request, PortletHolder portletHolder) {
		String portletId = portletHolder.getPortletId();
		StringBuffer sb = new StringBuffer(portletId).append("_").append("state");
		String state = request.getParameter(sb.toString());
		if (state != null) {
			try {
				WindowState windowState = new WindowState(state);
				portletHolder.setWindowState(windowState);
			} catch (WindowStateException wse){
				wse.printStackTrace();
			}
		}
	}

}
