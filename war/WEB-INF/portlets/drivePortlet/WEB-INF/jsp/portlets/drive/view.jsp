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

<table class="paginated">
<thead>
  <tr>
    <th>Title</th>
    <th>Size</th>
  </tr>
</thead>
<tbody>
<%
List<File> result = new ArrayList<File>();
Files.List fileList = (Files.List)renderRequest.getAttribute("net.tinyportal.portlet.drive");
do {
	try {
		FileList files = fileList.execute();
			for (File f : files.getItems()) {
				%>
				  <tr>
				  <% if (!f.getMimeType().equals("application/vnd.google-apps.folder")) { %>
	    				<td>
	    					<img src="<%=f.getIconLink() %>"/>
	    				    <a href="<%= f.getDownloadUrl()%>"><%= f.getTitle()%></a>
	    				</td>
	    				<td>
	    					<% String size = (f.getFileSize()==null)?"unknow":f.getFileSize().toString()+" b"; %>
	    					<%= size%>
	    				</td>
    				<% } else { %>
    				<td>
    				<img src="<%=f.getIconLink() %>"/>
						<a href="<portlet:actionURL>
    							<portlet:param name="type" value="<%= f.getMimeType()%>" />
    							<portlet:param name="id" value="<%=f.getId()%>" />
    						</portlet:actionURL>"><%= f.getTitle()%>
    					</a>
    				</td>
    				<td>

    				</td>
    				<% } %>
  				  </tr>
				<%				
			}
		
		result.addAll(files.getItems());
		fileList.setPageToken(files.getNextPageToken());
		} catch (IOException e) {
			fileList.setPageToken(null);
		}
} while (fileList.getPageToken() != null &&
		fileList.getPageToken().length() > 0);
%>
</tbody>
</table>

<script type="text/javascript">

$(document).ready(function() { 
	$('table.paginated').each(function() {
	    var currentPage = 0;
	    var numPerPage = 10;
	    var $table = $(this);
	    $table.bind('repaginate', function() {
	        $table.find('tbody tr').hide().slice(currentPage * numPerPage, (currentPage + 1) * numPerPage).show();
	    });
	    $table.trigger('repaginate');
	    var numRows = $table.find('tbody tr').length;
	    var numPages = Math.ceil(numRows / numPerPage);
	    var $pager = $('<div class="pager"></div>');
	    for (var page = 0; page < numPages; page++) {
	        $('<span class="page-number"></span>').text(page + 1+ " ").bind('click', {
	            newPage: page
	        }, function(event) {
	            currentPage = event.data['newPage'];
	            $table.trigger('repaginate');
	            $(this).addClass('active').siblings().removeClass('active');
	        }).appendTo($pager).addClass('clickable');
	    }
	    $pager.insertBefore($table).find('span.page-number:first').addClass('active');
	});
});
</script>