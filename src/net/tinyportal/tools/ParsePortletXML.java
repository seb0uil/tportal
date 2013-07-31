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

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

public class ParsePortletXML implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -3771924265206993197L;

	public static List<PortletXML> parse(File xmlDocument) throws Exception{
		try {
			File portletPath = xmlDocument.getParentFile().getParentFile();

			//On crée une instance de SAXBuilder
			SAXBuilder sxb = new SAXBuilder();
			//On crée un nouveau document JDOM avec en argument le fichier XML
			//Le parsing est terminé ;)
			Document document = sxb.build(xmlDocument);


			//On initialise un nouvel élément racine avec l'élément racine du document.
			Element racine = document.getRootElement();

			racine.getChildren();
			Namespace portletNS = racine.getNamespace();
			List<Element> portletsElement = racine.getChildren("portlet",portletNS);
			List<PortletXML> portletsXML = new ArrayList<PortletXML>(); 
			for (Element portletElement : portletsElement) {
				portletsXML.add(new PortletXML(portletElement, portletNS, portletPath)); 
			}
			return portletsXML;
		}
		catch(Exception e){
			e.printStackTrace();
			throw e;
		}	      
	}

}