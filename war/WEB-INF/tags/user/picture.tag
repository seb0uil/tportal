<%@ include file="init.tag" %><%
String picture = null;
System.out.println("--- " + userInfo);
if (userInfo == null) {
	picture = Constant.google_default_picture;
} else {
	picture = (userInfo.getPicture()==null)?Constant.google_default_picture:userInfo.getPicture();
}
%><%=picture%>