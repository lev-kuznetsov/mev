<table class="table table-hover table-condensed">
          
            <thead>
              <tr>
                <th>Name</th>
              </tr>
            </thead>
            
            
            
            <tbody>
              <tr ng-repeat="row in datasets">
                <td><a ng-click='redirect({{row}})'>{{row}}</a></td>
              <tr>
            </tbody>
            
</table>