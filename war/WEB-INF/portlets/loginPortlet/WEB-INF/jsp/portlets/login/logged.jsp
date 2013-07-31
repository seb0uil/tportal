<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>

<%@page import="com.google.api.services.oauth2.model.Userinfo"%>
<%@page import="net.tinyportal.Constant"%>

<portlet:defineObjects/>


<%Userinfo userInfo = (Userinfo)renderRequest.getAttribute(Constant.portlet_properties_prefix+ "gapi.user.info"); %>

<%=userInfo.getEmail()%>