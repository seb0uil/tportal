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

package net.tinyportal.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.UnavailableException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.tinyportal.Constant;
import net.tinyportal.Portal;
import net.tinyportal.bean.PortletHolder;
import net.tinyportal.javax.portlet.TpActionRequest;
import net.tinyportal.javax.portlet.TpActionResponse;
import net.tinyportal.javax.portlet.TpPortletContext;
import net.tinyportal.javax.portlet.TpRenderRequest;
import net.tinyportal.javax.portlet.TpRenderResponse;

public class Portlet extends HttpServlet  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7934374005770615573L;

	/**
	 * A l'initialisation de la servlet, on va initialiser les différents portlets présent<br/>
	 * <br/>
	 * Pour cela, on va parcourir le répertoire des portlets, les initialiser un a un
	 */
	public void init(ServletConfig config) {
		try {
			super.init(config);
			/*
			 * On va charger les portlets disponibles
			 */
			ServletContext context = getServletContext();

			/*
			 * On va charger les portlets présent dans le répertoire indiqué
			 */
			PortletLoader pLoader = new PortletLoader(context);
			String portlets_real_Path = context.getRealPath(Constant.portlets_path);
			File portletsPathFile = new File(portlets_real_Path);
			File[] portletList = portletsPathFile.listFiles();
			for (File portlet_directory : portletList) {
				try {
					Portal.addPortlets(pLoader.load(portlet_directory));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String portletName = request.getParameter("portlet");

			System.out.println("Get portlet " + portletName);

			/* verification d'usage */
			if (portletName==null) return; 

			/*
			 * On recupère le portletHolder ou on en crée un nouveau si besoin
			 */
			HttpSession session = request.getSession(true);
			PortletHolder portletHolder = (PortletHolder)session.getAttribute(portletName);
			if (portletHolder == null) {
				portletHolder = Portal.getPortlet(portletName);
				portletHolder.setActionResponse(null);
				portletHolder.setRenderResponse(null);
				portletHolder.setRenderRequest(null);
				session.setAttribute(portletName, portletHolder);
			} else {
				/*
				 * Dans tout les cas, on repositionne le portlet a partir
				 * de celui de référence, dans le cas contraire, cela ne
				 * fonctionne curieusement pas sur GAE
				 */
				portletHolder.setPortlet(
						Portal.getPortlet(portletName).getPortlet() );
			}


			String portletId = portletHolder.getPortletId();
			System.out.println("portletId " + portletId);

			GenericPortlet portlet = portletHolder.getPortlet();
			System.out.println("Get portlet " + portlet.toString());

			TpRenderResponse TPresponse = new TpRenderResponse(response, portletId);
			System.out.println("Get response" + TPresponse.toString());

			TpPortletContext tpPortletContext = (TpPortletContext) portlet.getPortletContext();
			System.out.println("Get context" + tpPortletContext);

			TpRenderRequest TPrequest = new TpRenderRequest(portletId, request, tpPortletContext);
			System.out.println("Get request" + TPrequest);


			request.setAttribute("net.tinyportal.windowState",portletHolder.getWindowState());
			request.setAttribute("net.tinyportal.portletMode",portletHolder.getPortletMode());
			request.setAttribute("net.tinyportal.portletPreference",portletHolder.getPreferences());

			portletHolder.setRenderRequest(TPrequest);
			portletHolder.setRenderResponse(TPresponse);
			portletHolder.setPortletConfig(portlet.getPortletConfig());

			System.out.println("Going to render portlet");
			try {
				/*
				 * On effectue les taches d'ACTION
				 */
				StringBuffer sb = new StringBuffer(portletId).append("_").append("action");
				String action = request.getParameter(sb.toString());
				if ("action".equalsIgnoreCase(action)) {
					/* Going Action */
					TpPortletContext TpPortletContext = (TpPortletContext) portlet.getPortletContext();
					TpActionRequest actionRequest = new TpActionRequest(portletHolder.getPortletId(),request,TpPortletContext);
					TpActionResponse actionResponse = new TpActionResponse(portletHolder,response);
					Portal.setPortletParameters(request, portletHolder, actionRequest);

					try {
						portlet.processAction(actionRequest, actionResponse);
						if (actionResponse.isRedirected())
							response.sendRedirect(actionResponse.getRedirection());
					} catch (IllegalStateException ise) {
						actionResponse.cancelRedirected();
					}
					portletHolder.setActionResponse(actionResponse);


				} else if ("render".equalsIgnoreCase(action)) {
					if (portletHolder.getActionResponse()!=null) 
						TPrequest.getParameterMap().putAll(portletHolder.getActionResponse().getRenderParametersMap());
					Portal.setPortletParameters(request, portletHolder, TPrequest);

				}

				Portal.setWindowState(request, portletHolder);
				Portal.setPortletMode(request, portletHolder);

				portlet.render(TPrequest,TPresponse);


			} catch (UnavailableException ue) {
				portlet.destroy();
				Portal.addDisabledPortlet(portletName);

			} catch (PortletException e) {
				log(e.getMessage());
				portletHolder.setTitle("Erreur sur le portlet");
				portletHolder.setContent("/!\\ Erreur !!" + e.getMessage());
			}

			Enumeration eAttribute = TPrequest.getAttributeNames();
			while (eAttribute.hasMoreElements()) {
				String attribute = (String) eAttribute.nextElement();
				Object value = TPrequest.getAttribute(attribute);
				request.setAttribute(attribute, value);
			}

			request.setAttribute(Constant.portlet_request, TPrequest);
			request.setAttribute(Constant.portlet_response, TPresponse);
			request.setAttribute(Constant.portlet_config, portletHolder.getPortletConfig());

			System.out.println("Going to dispatch portlet");
			String path = (String)TPrequest.getAttribute(Constant.portlet_dispatcher);
			if (path !=null) {
				String portalJsp = (TPrequest.getContextPath()+path).substring(2);

				ServletContext context = getServletContext();

				System.out.println("Going to dispatch with /" + portalJsp);
				//				HttpServletResponse newResponse = new FictiveHttpServletResponse((HttpServletResponse) response);				
				RequestDispatcher servletDispatcher = context.getRequestDispatcher("/"+portalJsp);			
				servletDispatcher.include(request, response);
				//				TPresponse.getWriter().write(response.getWriter().);
			} 
			else {
				response.setContentType("text/html; charset=UTF-8");
				response.getWriter().print(
						TPresponse.getStringContent());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			/*
			 * On retire de la requête les éléments spécifiques du portlet
			 */
			Enumeration attributes = request.getAttributeNames();
			while (attributes.hasMoreElements()) {
				String attr = (String)attributes.nextElement();
				if (attr.startsWith(Constant.portlet_properties_prefix)) {
					request.removeAttribute(attr);
				}
			}
			
			request.removeAttribute(Constant.portlet_request);
			request.removeAttribute(Constant.portlet_response);
			request.removeAttribute(Constant.portlet_config);
		}
	
}

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doGet(request, response);
}
}
