
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
	        <li id="tcga_tab"><a href="#tcga" data-toggle="tab" target="_self" >Import TCGA Datasets</a></li>
	        <li id="google_tab" ng-show="googleDrive.signedIn"><a href="#google" data-toggle="tab" target="_self" >Google Drive</a></li>
	        <li id="geods_tab"><a href="#geods" data-toggle="tab" target="_self" >Import GEO Datasets</a></li>
	      </ul>
	 	  
	      <div class="tab-content" id="import">
	         
	        <div class="tab-pane" id="tcga">
			  <div id="presetMgr" preset-manager ng-controller="PresetManagerController"></div>
	        </div>
	        
	        <div class="tab-pane" id="geods">
			  <div id="geodsImportMgr" mev-geods-import-directive ng-controller="MevGeodsImportCtrl"></div>
	        </div>
	        
	        <div class="tab-pane" id="google">
				
				<div class="geo_import_container" ng-show="googleDrive.signedIn">
					<table class="mev-table-tight table table-striped" style="font-size:14px;">
						<thead>
							<tr>
								<th>File Name</th>
								<th>Import</th>
							</tr>
						</thead>
						<tbody>
							<tr ng-repeat="file in googleDrive.files">
								<td>{{file.name}}</td>
								<td>
									<a class="btn btn-small btn-success" href="" ng-click="postDriveFile(file.id)">Import</a>
								</td>
							</tr>
						</tbody>
					</table>
					
				</div>
				
	        </div>
	        
	      </div>
	      
	    </div>
	    
	  </div>
	
	</div>
</div>