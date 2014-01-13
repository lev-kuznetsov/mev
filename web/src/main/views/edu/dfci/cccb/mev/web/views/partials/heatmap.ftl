<div class="navbar navbar-fixed-top">
  <div class="navbar-inner">
    <a class="brand" href="#"><span app-Name></span></a>
    <ul class="nav">
      
    </ul>
    <ul class="nav pull-right">
		<li ng-repeat="link in menu"><a href="{{link.url}}">{{link.value}}</a></li>
	</ul>
  </div>
</div>

<div class="row">
	<div heatmap-Panels></div>
</div>



