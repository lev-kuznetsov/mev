<form class="form-horizontal">
  
    <div class="control-group">
      <label for="inputAnalysisType" class="control-label">Type</label>
      <div class="controls">
        <select>
        <option>K-Means</option>
        <option>K-Medians</option>
        </select>
      </div>
    </div>
    
    <div class="control-group">
      <label for="inputAnalysisName" class="control-label">Clusters</label>
      <div class="controls">
        <input id="inputAnalysisClusters" placeholder="10">
      </div>
    </div>
    
    
    <div class="control-group">
      <label for="inputAnalysisName" class="control-label">Cluster Dimension</label>
      <div class="controls">
        <label class="radio">
          <input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" checked>
          Cluster Samples
        </label>
        <label class="radio">
          <input type="radio" name="optionsRadios" id="optionsRadios2" value="option2">
          Cluster Genes
        </label>
        
      </div>

</form>



