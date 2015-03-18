<table class="table table-hover table-condensed">
			<thead>
              <tr>
                <th>Your datasets</th>
              </tr>
            </thead>
            <tbody>
              <tr ng-show="datasets.length>0" ng-repeat="row in datasets">
                <!--  <td><a href="#/dataset/{{row}}"> <i class="icon-file"></i> {{row}}</a></td> -->
                <td><a href="#/dataset/{{row}}" ui-sref="root.dataset.home({datasetId: row})"> <i class="icon-file"></i> {{row}}</a></td>
              <tr>
              <tr ng-show="datasets.length<=0" ">
                <td>Use the button above to upoload your data or import a TCGA dataset on the right</td>
              <tr>
            </tbody>
</table>