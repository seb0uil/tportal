<%@page import="com.google.api.services.oauth2.model.Userinfo"%>
<%@page import="net.tinyportal.Constant"%>
<%@page import="com.google.api.services.drive.Drive"%>
<%@page import="com.google.api.services.drive.Drive.Files"%>
<%@page import="com.google.api.services.drive.model.File"%>
<%@page import="com.google.api.services.drive.model.FileList"%>
<%@page import="java.util.List, java.util.ArrayList"%>
<%@page import="java.io.IOException"%>

<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>


<portlet:defineObjects/>

<%

List<File> result = new ArrayList<File>();
Files.List fileList = (Files.List)renderRequest.getAttribute("net.tinyportal.portlet.drive");

do {
	try {
		FileList files = fileList.execute();
			for (File f : files.getItems()) {
				%>
				<li> <%= f.getTitle()%> - <%= f.getMimeType()%> - <a href="<portlet:actionURL><portlet:param name="action" value="<%=f.getId()%>" /></portlet:actionURL>"><%= f.getId()%></a><br/> </li>
				<%	
	//			System.out.println(f.getTitle() + " - " + f.getMimeType() + " - " + f.getId());
				
			}
		
		result.addAll(files.getItems());
		fileList.setPageToken(files.getNextPageToken());
		} catch (IOException e) {
			fileList.setPageToken(null);
		}
} while (fileList.getPageToken() != null &&
		fileList.getPageToken().length() > 0);
%>