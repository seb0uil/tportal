<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet"%>

<%@page import="com.google.api.services.oauth2.model.Userinfo"%>
<%@page import="net.tinyportal.Constant"%>
<%@taglib tagdir="/WEB-INF/tags/user" prefix="u"%>

<portlet:defineObjects />
<img src="<u:picture/>" />
<div class="nav navbar-nav pull-right">
<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#"><u:email /><b class="caret"></b></a>
	<ul class="dropdown-menu">
		<li><a href="#">Mon compte</a></li>
		<li class="divider"></li>
		<li><a href="#">Déconnexion</a></li>
	</ul>
</li>
</div>