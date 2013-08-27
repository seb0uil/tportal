<%@ include file="init.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<title>Starter Template for Bootstrap</title>

<%@ include file="css_header.jsp"%>
<%@ include file="js_header.jsp"%>

<%@taglib tagdir="/WEB-INF/tags/portlet" prefix="p"%>

</head>

<body>
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".nav-collapse">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
			</button>
			<a id="pop" href="#" class="TPpopover navbar-brand" data-toggle="popover" title="A Title" data-content="And here's some amazing content. It's very engaging. right?"> <%out.print(Constant.portal_context.getPortalInfo());%></a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="#">Home</a></li>
            <li><a href="#about">About</a></li>
            <li><a href="#contact">Contact</a></li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown <b class="caret"></b></a>
              <ul class="dropdown-menu">
                <li><a href="#">Action</a></li>
                <li><a href="#">Another action</a></li>
                <li><a href="#">Something else here</a></li>
                <li class="divider"></li>
                <li class="dropdown-header">Nav header</li>
                <li><a href="#">Separated link</a></li>
                <li><a href="#">One more separated link</a></li>
              </ul>
            </li>
          </ul>
          <span class="navbar-form navbar-right">
			<portal:portlet name="LoginPortlet" />
          </span>
        </div><!--/.navbar-collapse -->
      </div>
    </div>
	<div class="container">
		<div class="starter-template">
			<div class="row">
				<div class="col-lg-12">
					<p:portlet name="HtmlPresentationPortlet"/>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-6" style="overflow-y: auto;">
					<p:portlet name="DrivePortlet" />
				</div>
				<div class="col-lg-5 col-lg-offset-1" style="overflow-y: visible;">
					<p:portlet name="PlusPortlet" />
				</div>
			</div>

			<!-- Button trigger modal -->
			<a data-toggle="modal" href="#myModal" class="btn btn-primary btn-large">Launch demo modal</a> <a data-toggle="modal" href="test.html" data-target="#myModal">Click me</a>
			<!-- Modal -->
			<div class="modal fade" id="myModal">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
							<h4 class="modal-title">Bienvenue</h4>
						</div>
						<div class="modal-body">...</div>
						<div class="modal-footer">
							<a href="#" class="btn">Close</a> <a href="#" class="btn btn-primary">Save changes</a>
						</div>
					</div>
					<!-- /.modal-content -->
				</div>
				<!-- /.modal-dialog -->
			</div>
			<!-- /.modal -->

		</div>
		<div class="alert alert-danger">
			<button type="button" class="close" data-dismiss="alert">×</button>
			<strong>Oh snap!</strong> <a href="#" class="alert-link">Change a few things up</a> and try submitting again.
		</div>
	</div>
	<!-- /.container -->
</body>
</html>
