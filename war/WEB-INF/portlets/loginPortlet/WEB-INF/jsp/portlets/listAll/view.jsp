<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>

<portlet:defineObjects/>

<%
Set<String> portlets = (Set<String>)renderRequest.getAttribute("portlets");

Iterator<String> it = portlets.iterator();
while (it.hasNext()) {
	String portlet = (String)it.next();
%>
	<%=portlet %> <br/>	
<%
}

%>