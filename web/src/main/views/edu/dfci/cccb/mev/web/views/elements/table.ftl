<table class="table table-hover table-bordered">
<thead>
  <tr>
        <th 
          ng-repeat="header in data.headers">
          {{header.value}}
        </th>
  </tr>
</thead>
<tbody>
  <tr ng-repeat="row in data.rows">
        <td ng-repeat="cell in row">
          <p>{{cell.value}}</p>
        </td>
  </tr>
</tbody>
</table>