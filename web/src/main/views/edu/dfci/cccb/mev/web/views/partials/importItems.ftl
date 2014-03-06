
<div class="navbar navbar-fixed-top">

	<div class="navbar-inner">
		<ul class="nav pull-right">
			<a class="brand" href="/">mev<img src="/container/images/favicon/favicon-32x32.png"/></a>
		</ul>

	</div>
</div>
     		
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
	    <div class="row">
	      <ul class="nav nav-tabs" id="importTabs">
	        <li class="active"><a href="#current" data-toggle="tab" ng-click="loadUploads()">Files</a></li>
	        <li><a href="#googleDrive" data-toggle="tab">Google Drive</a></li>
	        <li><a href="#tcga" data-toggle="tab">TCGA</a></li>
	      </ul>
	 	  
	      <div class="tab-content">
	      
	         <div class="tab-pane active" id="current">
	         
	            <div class="row">
	            
	              <div class="col-md-12">
	                <div uploads-Table uploads="userUploads"></div>
	              </div>
	              
	            </div>
	           
	         </div>
	        
	        <div class="tab-pane" id="tcga">
			  <div id="presetMgr" preset-manager ng-controller="PresetManagerController"></div>
	        </div>
	       
	        
	      </div>
	      
	    </div>
	    
	  </div>
	
	</div>
</div>