
<mev-main-menu></mev-main-menu>

<a class="brand" href="/"></a>
     		
<div class="container-fluid imports-container">

	<div class="row">
	
	  <div class="col-md-3">	
	  
	    <div class="row">
	        <div class="col-md-10 col-md-offset-1"  id="userUploadsPanel" >
	        	
		            <div class="row" >
		                <div class='uploadDrag'></div>
		            </div>
		            <div class="row">
		            	<div uploads-Table uploads="userUploads"></div>
		            </div>
	            
	        </div>
	    </div>
	  
	  </div>
	
	
	  <div class="col-md-8">
	  
	  
	    <div class="row">
	      <ul class="nav nav-tabs" id="importTabs">
<!-- 	        <li class="active"><a href="#current" data-toggle="tab" ng-click="loadUploads()" target="_self">Files</a></li> -->
	        <!--<li><a href="#googleDrive" data-toggle="tab">Google Drive</a></li>-->
	        <li class="active"><a href="#tcga" data-toggle="tab" target="_self" >Import TCGA Datasets</a></li>
	      </ul>
	 	  
	      <div class="tab-content" id="import">
	         
	        <div class="tab-pane active" id="tcga">
			  <div id="presetMgr" preset-manager ng-controller="PresetManagerController">
			  </div>
	        </div>
	       
	        
	      </div>
	      
	    </div>
	    
	  </div>
	
	</div>
</div>