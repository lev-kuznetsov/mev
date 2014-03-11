
<div id="setmanagerAccordion" class="panel-group">

	<div class="panel panel-default">
		<div class="panel-heading" ng-dblclick="sayHelloCtl()">
			<a class="accordion-toggle" data-parent="setmanagerAccordion" data-toggle="collapse"
				href="#collapseSetManagerColumns"> Column Sets </a>			
		</div><!-- End Heading Div -->

		<div id="collapseSetManagerColumns" class="panel-collapse">
			<div class="panel-body">
				<selection-set-list 
					mev-selections="heatmapData.column.selections" 
					mev-base-url="/annotations/{{heatmapId}}/annotation/column"
					mev-demintion="column">
					You may define sets using <a href="" ng-click="showAnnotations(selection, 'column')">column annotations</a> or by performing a clustering analysis.				
				</selection-set-list>				
			</div>
		</div><!-- End Accordion Body -->
	</div><!-- End Accordion Grouping -->


	<div class="panel panel-default">

		<div class="panel-heading">
			<a class="accordion-toggle" data-parent="setmanagerAccordion" data-toggle="collapse"
				href="#collapseSetManagerRows"> Row Sets </a>
		</div>		

		<div id="collapseSetManagerRows" class="panel-collapse">
			<div class="panel-body">
			
				<selection-set-list 
					mev-selections="heatmapData.row.selections" 
					mev-base-url="/annotations/{{heatmapId}}/annotation/row"
					mev-demintion="row">
					You may define sets uploading your <a href="" ng-click="showAnnotations(selection, 'row')">row annotations</a> or by looking up <a href="" ng-click="showAnnotations(selection, 'row', 'probe')">probe annotations</a> from our database.				
				</selection-set-list>
				
		</div>
		</div><!-- End Accordion Body -->
	</div><!-- End Accordion Grouping -->

</div><!-- End Accordion Definition -->

