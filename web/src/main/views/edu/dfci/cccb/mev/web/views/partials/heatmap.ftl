
<div class="row-fluid">
	<div heatmap-Panels></div>
</div>


<div id="loading" 
  class="modal hide fade" 
  tabindex="-1" 
  role="dialog" 
  aria-labelledby="loadingModalLabel" 
  aria-hidden="true">
  <div class="modal-body">
    <p> Please wait while your dataset loads... </p>
  </div>
</div>

<!-- Modal -->
<div id="settingsModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="settingsModalLabel" aria-hidden="true">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
    <h3 id="settingsModalLabel">Heatmap Settings</h3>
  </div>
  <div class="modal-body">
    <div class="row-fluid">

          <select ng-model="selectedColor" ng-options="selection for selection in colorOptions" />
          <select ng-model="hotThreshold" ng-options="selection for selection in hotThresholdOptions" />
          <select ng-model="coldThreshold" ng-options="selection for selection in coldThresholdOptions" />

      
      
    </div>
  </div>
  <div class="modal-footer">
    <button class="btn" ng-click="defaultColors()">Default</button>
    <button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">Save</button>
  </div>
</div>
