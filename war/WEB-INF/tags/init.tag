<%@tag import="java.util.*"%>
<%@tag import="javax.portlet.*"%>
<%@tag import="com.google.api.services.oauth2.model.Userinfo"%>
<%@tag import="net.tinyportal.Constant"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@taglib uri="/WEB-INF/tld/taglib.tld" prefix="portal"%>
<%@taglib uri="http://java.sun.com/portlet" prefix="portlet"%>


<% RenderRequest renderRequest = (RenderRequest)request.getAttribute(Constant.portlet_request); %>
<% RenderResponse renderResponse = (RenderResponse)request.getAttribute(Constant.portlet_response); %>
<% PortletConfig  portletConfig  = (PortletConfig )request.getAttribute(Constant.portlet_config); %>

