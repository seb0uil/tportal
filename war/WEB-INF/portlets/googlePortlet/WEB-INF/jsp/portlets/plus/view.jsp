<%@page import="com.google.api.services.oauth2.model.Userinfo"%>
<%@page import="net.tinyportal.Constant"%>

<%@page import="com.google.api.services.plus.model.Activity, com.google.api.services.plus.model.Comment"%>
<%@page import="com.google.api.services.plus.model.PeopleFeed"%>

<%@page import="java.util.List, java.util.ArrayList"%>
<%@page import="java.io.IOException"%>

<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>


<portlet:defineObjects/>

<%
List<Activity> activities = (List<Activity>)renderRequest.getAttribute("net.tinyportal.portlet.plus.activities");
List<Comment> comments = (List<Comment>)renderRequest.getAttribute("net.tinyportal.portlet.plus.comments");
Activity activity = activities.get(0);
%>
		<script>
			$(document).ready(function() {
				$('.TPpopover').popover({
					placement : 'auto top'
				});
				$.getJSON( "/people?service=PLUS", function(result) {
					$.each(result, function(cpt) {
					  data = result[cpt];
					  //alert(data.actor.displayName);
					  var plusPost = $("#plusPost");
					  var post = plusPost[0].cloneNode(true);
					  $(post).find('.plusAuthor')[0].innerHTML = data.actor.displayName;
					  $(post).find('.plusContent')[0].innerHTML= data.object.content;
					  $(post).attr("id","post_"+data.id);
					  $(post).attr("class","plusPost");
					  $(post).attr("activity",data.id);
					  $(post).appendTo(plusPost.parent());
					  $(post).show();
					}
					);

				});
				$(function () {
					function plusPost() {
						$.each($(".plusPost"), function(index) {
							arr = "";
							$.getJSON( "/comment/"+$(this).attr("activity")+"?service=PLUS", function(result) {
							$.each(result, function(cpt) {
								data = result[cpt];
								arr+="<img data-content='"+data.object.content+"' data-original-title='"+data.actor.displayName+"' data-toggle='popover' class='TPpopover' src='"+data.actor.image.url+"' title=''>";
							});
							console.log(arr);
							$($($(".plusPost")[index]).find(".plusComment")).replaceWith($(arr));
							arr = "";
							});
						
						});	
						$('.TPpopover').popover({
							placement : 'auto top'
						});
					};
					setInterval(plusPost,5000);
					plusPost();
				});
				
				
				$('.TPpopover').popover({
					placement : 'auto top'
				});
			});
		</script>


<div id="plusPost" style="display:none;">
	<div class="plusAuthor"><%=activity.getActor().getDisplayName() %></div>
	<div class="plusDate"><%=activity.getPublished() %></div>
	<div class="plusContent"><%=activities.get(0).getObject().getContent()%></div>
	<div class="plusComment">
	</div>
</div>