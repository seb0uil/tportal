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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.servlet.ServletContext;

import net.tinyportal.Constant;
import net.tinyportal.PortletManager;
import net.tinyportal.bean.PortletHolder;
import net.tinyportal.javax.portlet.TpPortletConfig;
import net.tinyportal.javax.portlet.TpPortletContext;
import net.tinyportal.tools.ParsePortletXML;
import net.tinyportal.tools.PortletXML;
import net.tinyportal.tools.TpClassLoader;

public class PortletLoader {

	ServletContext context;
	
	public PortletLoader(ServletContext context) {
		this.context = context;
	}
	
	/*
	 * On charge les portlets a partir du chemin fournit
	 */
	public Map<String, PortletHolder> load(String portlet_path, List<String> portletsList) throws Exception {
		return load(new File(portlet_path), portletsList);
	}

	/**
	 * Charge le portlet passé en paramètre dans le chemin
	 * passé en paramètre
	 * @param portletPathFile
	 * @throws Exception
	 */
	public Map<String, PortletHolder> load(File portletPathFile, String portletName) throws Exception {
		List<String> portletsList = new ArrayList<String>();
		portletsList.add(portletName);
		return load(portletPathFile, portletsList);
	}
	
	/**
	 * Charge l'ensemble des portlets disponible dans le chemin
	 * passé en paramètre
	 * @param portletPathFile
	 * @throws Exception
	 */
	public Map<String, PortletHolder> load(File portletPathFile) throws Exception {
		List<String> portletsList = null;
		return load(portletPathFile, portletsList);
	}
//	PortletManager.addPortlet(portletHolder.getPortletName(), portletHolder);
	/**
	 * Charge, dans le chemin passé en paramètre, les portlets
	 * présents dans la liste, si ceux-ci sont disponibles
	 * @param portletPathFile
	 * @param portletsList
	 * @throws Exception
	 */
	public Map<String, PortletHolder> load(File portletPathFile, List<String> portletsList) throws Exception {
		if (!portletPathFile.isDirectory()) throw new Exception(portletPathFile.getName() + " n'est pas un répertoire");
		
		Map<String, PortletHolder> portletPool = new HashMap<String, PortletHolder>();
		
		String portlet_directory = portletPathFile.getName();
		String portlet_path = portletPathFile.getCanonicalPath(); 
		
		StringBuffer portlet_path_xml = new StringBuffer(portlet_path).append(Constant.portlet_xml); 
		File portletDotXml = new File(portlet_path_xml.toString());
		/*
		 * S'il n'y a pas de portlet.xml, ce n'est pas la peine d'aller plus loin!
		 */
		if (!portletDotXml.exists()) return portletPool;

		/*
		 * On complete le classpath avec celui du portlet
		 */
		List<URL> urls = new ArrayList<URL>();
		StringBuffer portlet_path_classes = new StringBuffer(portlet_path).append(Constant.portlet_classes);
		StringBuffer portlet_path_lib = new StringBuffer(portlet_path).append(Constant.portlet_lib);
		urls.add(new File(portlet_path_classes.toString()).toURI().toURL());
		File portletLibPath = new File(portlet_path_lib.toString());
		File[] portletLibs = portletLibPath.listFiles();
		if (portletLibs!=null)
			for (File portletLib : portletLibs)
				if (portletLib.getName().endsWith(Constant.jar_extension)) {
					urls.add(portletLib.toURI().toURL());
					System.out.println("Ajout " + portletLib.toURI().toURL());
				}
		URL url[] = (URL[])urls.toArray(new URL[0]);
		TpClassLoader loader = new TpClassLoader(url,Thread.currentThread().getContextClassLoader());

		/*
		 * On charge la configuration du portlet
		 */
		
		for (PortletXML portletXml_ : ParsePortletXML.parse(portletDotXml)) {
			String portletName = portletXml_.getPortletName();
			
			if (portletsList!=null && !portletsList.contains(portletName)) continue;
			if (!PortletManager.isActivePortlet(portletName)) continue;
			
			PortletXML portletXml = portletXml_;

			/*
			 * On crée un nouveau context pour le portlet
			 */
			String portletClass = portletXml_.getPortletClass();

			
			TpPortletContext tpPorletContext = null;
			if (PortletManager.isContextExist(portlet_directory)) {
				tpPorletContext = PortletManager.getContext(portlet_directory);
			} else {
				tpPorletContext = new TpPortletContext(context, portletXml );
				
				PortletManager.addContext(tpPorletContext);

			}

			/*
			 * On crée un configuration pour le portlet
			 */
			PortletConfig TPconfig = new TpPortletConfig(tpPorletContext, loader);
			
			try {
				/*
				 * On charge le portlet
				 */
				@SuppressWarnings("rawtypes")
				Class c = loader.loadClass(portletClass);
				GenericPortlet portlet = (GenericPortlet)c.newInstance();
				portlet.init(TPconfig);

				PortletHolder portletHolder = new PortletHolder(portlet,portletXml);
				portletPool.put(portletHolder.getPortletName(), portletHolder);
			} catch (PortletException pe) {
				//Exception levée lors de l'initialisation du portlet, on passe
				// la suite du traitement du portlet
//				return;
			} catch (RuntimeException re) {
//				return;
			} catch (Exception e) {
				e.printStackTrace();
			} catch (Throwable t) {
				t.printStackTrace();
				PortletManager.addDisabledPortlet(portletName);
			}
			
		}
		return portletPool;
		
	}
}
