<%@tag import="javax.portlet.PortletMode"%>
<%@ include file="init.tag" %>

<%@tag import="javax.servlet.http.HttpSession"%>
<%@tag import="net.tinyportal.bean.PortletHolder"%>
<%@tag import="net.tinyportal.PortletManager"%>

<%@ attribute name="name" required="true" rtexprvalue="true"
	type="java.lang.String" description="Nom de la zone a afficher"%>

<%@ attribute name="header" required="false" rtexprvalue="true"
	type="java.lang.Boolean" description="Affiche ou non les controles du portlet"%>
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
PortletMode portletMode = (PortletMode)session.getAttribute("net.tinyportal.portletMode."+portletId);
System.out.println(portletId +" -- " + portletMode);
%>	
<div class='portlet'>
  <% if (header==null || header) { %>
  <!-- Collect the nav links, forms, and other content for toggling -->
  <div class="collapse navbar-collapse navbar-ex1-collapse portlet-menu-bar">
    <ul class="nav navbar-nav navbar-right portlet-menu" style="display:none;">
      <li class="dropdown">
        <a href="#" class="dropdown-toggle portlet-menu" data-toggle="dropdown"><b class="caret"></b></a>
        <ul class="dropdown-menu">
        <%-- VIEW --%>
          <%
          if (portletHolder.isPortletModeAllowed(PortletMode.VIEW)) {
          %>
	          <li>
	          	<a href="?<%=portletId%>_mode=VIEW">Affichage</a>
	          </li>
          <% } %>
        <%-- EDIT --%>
          <%
          if (portletHolder.isPortletModeAllowed(PortletMode.EDIT)) {
          %>
	          <li>
	          	<a href="?<%=portletId%>_mode=EDIT">Edition</a>
	          </li>
          <% } %>
        <%-- HELP --%>
          <%
          if (portletHolder.isPortletModeAllowed(PortletMode.HELP)) {
          %>
	          <li>
	          	<a href="?<%=portletId%>_mode=HELP">Aide</a>
	          </li>
          <% } %>
        </ul>
      </li>
    </ul>
  </div><!-- /.navbar-collapse -->

	
	 <script>
$( document ).ready(function() {
	$('.portlet-menu-bar').hover(
	function(){
		$(this).children().fadeIn(400);
		//$(this).children().show();
	}
	,function(){
		$(this).children().fadeOut(400);
		//$(this).children().hide();
		$(this).children('ul').children('li').attr('class','dropdown');
		}
	);
});

</script>
 <% } %>

	<portal:portlet name="${name}" />
</div>