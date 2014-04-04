      <form role="form">

          <div class="form-group">
            <label for="tTestName" class="control-label">Analysis Name</label>

            <input id="tTestName" ng-model="params.name" placeholder="Ex: My_Analysis_1">
          </div>
          
     </form> 
     		
     <button class="btn btn-success btn-block" ng-click="testInit()" data-dismiss="modal" aria-hidden="true">Analyze</button>