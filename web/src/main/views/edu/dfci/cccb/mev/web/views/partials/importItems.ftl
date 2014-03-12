<nav class="navbar navbar-default" role="navigation">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header navbar-right">
      <a class="navbar-brand brand" href="/">mev<img src="/container/images/favicon/favicon-32x32.png"/></a>
    </div>
</nav>

<a class="brand" href="/"></a>
     		
<div class="container-fluid imports-container">

	<div class="row">
	
	  <div class="col-md-3">
	  
	    <div class="row">
	        <div class="col-md-10 col-md-offset-1">
	        
	            <div class="row">
	                <div class='uploadDrag'></div>
	            </div>
	        </div>
	    </div>
	  
	  </div>
	
	
	  <div class="col-md-8">
	  
	    <tabset>
			<tab heading="Files">
				<div uploads-Table uploads="userUploads"></div>
			</tab>

			<tab heading="TCGA">
				<div id="presetMgr" preset-manager ng-controller="PresetManagerController">
			  </div>
			</tab>
		</tabset>
		

	    
	  </div>
	
	</div>
</div>