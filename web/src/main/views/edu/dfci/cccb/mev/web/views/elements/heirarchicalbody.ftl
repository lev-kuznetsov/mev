      <form class="form-horizontal">

            <div class="control-group">
          <label for="inputDataset" class="control-label">Dataset</label>
          <div class="controls">
                          <input type="text" id="inputDataset" placeholder="Dataset_1">
                  </div>
                </div>

          <div class="control-group">
            <label for="inputAnalysisName" class="control-label">Analysis Name</label>
            <div class="controls">
              <input type="password" id="inputAnalysisName" placeholder="Ex: My_Analysis_1">
            </div>
          </div>

          <div class="control-group">
            <label for="inputAnalysisName" class="control-label">Distance Metric</label>
            <div class="controls">
              <select ng-options="option.name for option in analysislist" ng-model="selectedanalysis"></select>
            </div>
          </div>
     </form> 