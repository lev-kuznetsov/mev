<!doctype html>
<html lang="en" ng-app="myApp">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>WebMev: Online Bioinformatics Data Presentation</title>
  

  <!-- Bootstrap -->
  <link rel="stylesheet" href="/resources/static/webjars/bootstrap/${buildProperties['bootstrap.version']}/css/bootstrap.min.css"/>
  <link rel="stylesheet" href="/resources/static/webjars/bootstrap/${buildProperties['bootstrap.version']}/css/bootstrap-responsive.css"/>

  <!-- Effeckt -->
  <link rel="stylesheet" href="lib/effeckt/css/demo/demo.autoprefixed.css">
  <link rel="stylesheet" href="lib/effeckt/css/modules/off-screen-nav.css"/>
  <link rel="stylesheet" href="css/app.css"/>
</head>
<body>
  <div class="row">
  </div>
   <!-- Navbar -->

  <div class="navbar navbar-static-top">
    <div class="navbar-inner">
      <a class="brand" href="">WebMev: Online Bioinformatics Data Presentation</a>
      <ul class="nav pull-right">
        <li>
        <a class="off-screen-nav-button" data-effeckt="effeckt-off-screen-nav-right-overlay">
		<i class="icon-th-list"></i>
		</a></li>
      </ul>
    </div>
  </div>
  
  <nav class="effeckt-off-screen-nav" id="effeckt-off-screen-nav">
    <h4>
	  Menu
      <a href="" id="effeckt-off-screen-nav-close" class="effeckt-off-screen-nav-close">×</a>
    </h4>

    <ul>
      <li><a href="#/analyze">Analyze</a></li>
      <li><a href="#/features">Features</a></li>
      <li><a href="#/news">News</a></li>
      <li><a href="#/about">About</a></li>
      <li><a href="#/help">Help</a></li>
    </ul>
  </nav>
 
  <!-- View -->

  <div ng-view></div>

  <!-- Footer -->
  <hr>
  <div class="footer">
	<div class="row">
	<div class="span2 offset 1">
	  <a href="http://cccb.dfci.harvard.edu/"><img src="img/cccb-logo.jpg" class="img-polaroid"></a>
	</div>
	<div class="span2 offset1">
		<p class="lead">Product</p>
		<table class="table">
		  <tbody>
			<tr><td><a href="#/analyze">Tutorial</a></td></tr>
			<tr><td><a href="#/features">Features</a></td></tr>
			<tr><td><a href="#/news">News</a></td></tr>
		  </tbody>
		</table>
	</div>
	<div class="span2 offset1">
	  <p class="lead">Community</p>
		<table class="table">
		  <tbody>
			<tr><td><a href="https://www.facebook.com/dfci.cccb">Facebook</a></td></tr>
			<tr><td><a href="https://twitter.com/intent/user?screen_name=CCCBseq">Twitter</a></td></tr>
			<tr><td><a href="http://cccb.dfci.harvard.edu/cccb/about">Contact Us</a></td></tr>
		  </tbody>
		</table>
	</div>
	</div>
  </div>

  <!--Bootstrap -->
  <script src="/resources/static/webjars/bootstrap/${buildProperties['bootstrap.version']}/js/bootstrap.min.js"></script>
  <!--D3-->
  <script src="/resources/static/webjars/d3js/${buildProperties['d3.version']}/d3.min.js"></script>
  
  <!-- Effeckt -->
  
  <script src="/resources/static/webjars/jquery/${buildProperties['jquery.version']}/jquery.min.js"></script>
  
  <script>window.jQuery || document.write('<script src="/resources/static/webjars/jquery/${buildProperties['jquery.version']}/jquery.min.js"><\/script>')</script>
  <script src="lib/effeckt/js/modules/off-screen-nav.js"></script>
  
  <!--Angular-->
  <script src="/resources/static/webjars/angularjs/${buildProperties['angularjs.version']}/angular.min.js"></script>
  <script src="js/app.js"></script>
  <script src="js/services.js"></script>
  <script src="js/controllers.js"></script>
  <script src="js/filters.js"></script>
  <script src="js/directives.js"></script>
</body>
</html>
