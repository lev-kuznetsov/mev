
<div class="containter-fluid">

	<div class="row">
		<div heatmap-Panels></div>
	</div>
	
	
	<div id="loading" 
	  class="modal fade" 
	  tabindex="-1" 
	  role="dialog" 
	  aria-labelledby="loadingModalLabel" 
	  aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<p> Please wait while your dataset loads... </p>
				</div>
			</div>
		</div>
	</div>
	
	<!-- Modal -->
	<div id="settingsModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="settingsModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
			  <div class="modal-header">
			    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			    <h3 id="settingsModalLabel">Heatmap Settings</h3>
			  </div>
			  <div class="modal-body">
			    <div class="row-fluid">
			          Heatmap Color:
			          <select ng-model="selectedColor" ng-options="selection for selection in colorOptions"></select>
			    </div>
			    
			    
			  </div>
			  <div class="modal-footer">
			    <button class="btn" ng-click="defaultColors()">Default</button>
			    <button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">Save</button>
			  </div>
			</div>
		</div>
	</div>
</div>