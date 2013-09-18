<div class="page-header">

	<h1>Annotations Filter</h1>

</div>


<div class="row">
	<div class="span4">

		<ul class="thumbnails">
			<li class="span3" ng-repeat="field in fieldFilters">
				<div class="row">
					<p>{{field.variable}}</p><i class="icon-remove" ng-click="remFilter(field)"></i>
					
					<tuple-Selector inputfield="field" pushToQuery="updateQuery"></tuple-Selector>
					
				</div>
			</li>
			<li>
				<button class="btn btn-small btn-danger" ng-click="remAll()">Clear</button>
				<button class="btn btn-small btn-primary" ng-click="reqQuery(1)">Filter</button>
			</li>
		</ul>
		
	</div>
	
	<!-- Data Table -->
	<div class="span8">
	    <table class='table table-hover table-bordered'>
	    	<thead>
		        <tr>
		          <th ng-repeat="header in headers">{{header}}<i class="icon-plus-sign" ng-click="addFilter( header )" ></i></th>
		        </tr>
	    	</thead>
	    	<tbody>
	        	<tr ng-repeat="row in tuples">
	            	<td ng-repeat="cell in row">
	              		{{cell}}
	            	</td>
	    		</tr>
	    	</tbody>
	    </table>
	</div>
	
</div>