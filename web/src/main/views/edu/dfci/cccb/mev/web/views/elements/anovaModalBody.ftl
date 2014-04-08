      <form role="form">

          <div class="form-group">
            <label for="anovaName" class="control-label">Analysis Name</label>

            <input id="anovaName" ng-model="params.name" placeholder="Ex: My_Analysis_1">
          </div>
          
          <div class="form-group">
		    <label for="anovaDimension" class="control-label">Dimension</label>
		
		      <select id="anovaDimension" ng-model="params.dimension" ng-options="dimension.name for dimension in options.dimensions">
		      </select>
		
		  </div>
		  
		  <div class="form-group">
            <label for="anovaPValue" class="control-label">P-Value</label>

            <input id="anovaPValue" ng-model="params.pvalue" placeholder="">
          </div>
          
          <div class="form-group">
            <label for="anovaMTC" class="control-label">Multitest correction:</label>

            <input id="anovaMTC" type="checkbox" ng-model="params.mtc.value" 
            	ng-true-value="true" ng-false-value="false"></input>
          </div>
          
          <div class="form-group">
		    <label class="control-label">Selections</label>
		    <div class="container-fluid">
		    	<div class="md-col-12">
		    	
		    		<div class="row">
		    		
		    			<div class="md-col-1">
		    				<select id="anovaSelections" 
		    					ng-model="deckedSelection" 
		    					ng-options="selection.name for selection in heatmapData[params.dimension.value].selections">
		    				</select>
		    				<button type="button" class="btn btn-primary btn-sm" ng-click="addSelection(deckedSelection)">Add</button>
		    			</div>
		    		
		    			<div class="md-col-3">
		    				<div id="anovaSelections" ng-repeat="selection in params.selections">
						    	{{selection}} <span class="glyphicon glyphicon-remove" ng-click="params.selections.splice($index, 1)"></span>
						    </div>
		    			</div>
		    			
		    		</div>
		    	
		    	</div>
			    			    
		    </div>
		  </div>
          
     </form> 
     		
     <button class="btn btn-success btn-block" ng-click="testInit()" data-dismiss="modal" aria-hidden="true">Analyze</button>