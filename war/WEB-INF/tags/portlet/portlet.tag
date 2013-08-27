<%@ include file="init.tag" %>

<%@tag import="javax.servlet.http.HttpSession"%>
<%@tag import="net.tinyportal.bean.PortletHolder"%>
<%@tag import="net.tinyportal.PortletManager"%>

<%@ attribute name="name" required="true" rtexprvalue="true"
	type="java.lang.String" description="Nom de la zone a afficher"%>

<%
/*
 * On recupère le portletHolder ou on en crée un nouveau si besoin
 */
PortletHolder portletHolder = (PortletHolder)session.getAttribute(name);
if (portletHolder == null) {
	portletHolder = PortletManager.getPortlet(name.split("//")[0]);
	portletHolder.setActionResponse(null);
	portletHolder.setRenderResponse(null);
	portletHolder.setRenderRequest(null);
	session.setAttribute(name, portletHolder);
} else {
	/*
	 * Dans tout les cas, on repositionne le portlet a partir
	 * de celui de référence, dans le cas contraire, cela ne
	 * fonctionne curieusement pas sur GAE
	 */
	portletHolder.setPortlet(
			PortletManager.getPortlet(name).getPortlet() );
}

String portletId = portletHolder.getPortletId();
%>	
<div class='portlet'> 
<a href="?<%=portletId%>_mode=EDIT">EDIT</a>
	<portal:portlet name="${name}" />
</div>