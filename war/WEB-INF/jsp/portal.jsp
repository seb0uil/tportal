<%@ include file="init.jsp"%>

<html>
<head>
<link rel="stylesheet" href="default.css" type="text/css" media="screen" />
</head>

<body>
	<div id="header">
		<div id="top"></div>
		<div id="info-boxes">
			<div id="info-box1">
				<%
					out.print(Constant.portal_context.getPortalInfo());
				%> <a>go</a>
			</div>
			<div id="info-box2">
				<portal:portlet name="LoginPortlet" /> 
			</div>
		</div>
	</div>

	<div id="wrapper" class="portal">
		<div id="main">
			<div id="content" class="content">
				<div id="top-1" class="hundred">
					<portal:portlet name="DrivePortlet" /> 
				</div>

				<div id="top-2-1" class="fifty">

				</div>

				<div id="top-2-2" class="fifty">

				</div>

				<div id="top-1" class="hundred">

				</div>

			</div>
		</div>
		<div id="main-menu">
			<div id="favorites-menu" class="block">
				<h3 style="cursor: pointer;">Portlet</h3>
					<portal:portlet name="ListAllPortlet" /> 
			</div>
		</div>
		<div id="footer" style="position: static; padding: 0.75em 0pt; width: auto;">
		tPortal sample layout
		</div>
	</div>

</body>
</html>