
<!-- Page Hacks -->
<br>
<br>
<br>
<!-- End Page Hacks -->
     		
<div class="row-fluid">

  <div class="span3">
  
    <div class="row-fluid">
        <div class="span10 offset1">
            <div class="row-fluid">
                <p class="lead">Manage Datasets</p>
                <hr>
            </div>
            <div class="row-fluid">
                <div class='uploadDrag'></div>
            </div>
        </div>
    </div>
  
  </div>



  <div class="span8">
    <div class="row-fluid">
      <ul class="nav nav-tabs" id="importTabs">
        <li class="active"><a href="#current" data-toggle="tab" ng-click="loadUploads()">Files</a></li>
        <li><a href="#googleDrive" data-toggle="tab">Google Drive</a></li>
        <li><a href="#tcga" data-toggle="tab">TCGA</a></li>
      </ul>
 	  
      <div class="tab-content">
      
         <div class="tab-pane active" id="current">
         
            <div class="row-fluid">
            
              <div class="span12">
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