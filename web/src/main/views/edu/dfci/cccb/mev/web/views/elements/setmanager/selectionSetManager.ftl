<div id="setmanagerAccordion" class="accordion">

	<div class="accordion-group">
		<div class="accordion-heading">
			<a class="accordion-toggle" data-toggle="collapse"
				href="#collapseSetManagerColumns"> Column Sets </a> <a
				class="accordion-action-button"
				href="/annotations/{{heatmapId}}/annotation/column/new/">create new set from annotations</a>			
		</div><!-- End Heading Div -->

		<div id="collapseSetManagerColumns" class="accordion-body collapse">
			<div class="accordion-inner">
				<selection-set-list 
					mev-selections="heatmapData.column.selections" 
					mev-base-url="/annotations/{{heatmapId}}/annotation/column"
					mev-say-hello="sayHello()">
					You may define sets using <a href="/annotations/{{heatmapId}}/annotation/column/new/">column annotations</a> or by performing a clustering analysis.				
				</selection-set-list>				
			</div>
		</div><!-- End Accordion Body -->
	</div><!-- End Accordion Grouping -->


	<div class="accordion-group">

		<div class="accordion-heading">
			<a class="accordion-toggle" data-toggle="collapse"
				href="#collapseSetManagerRows"> Row Sets </a>
		</div>		

		<div id="collapseSetManagerRows" class="accordion-body collapse">
			<div class="accordion-inner">
			
				<selection-set-list 
					mev-selections="heatmapData.column.selections" 
					mev-base-url="/annotations/{{heatmapId}}/annotation/row"
					mev-say-hello="sayHello()">
					You may define sets by performing a clustering analysis.				
				</selection-set-list>
				
			</div>
		</div><!-- End Accordion Body -->
	</div><!-- End Accordion Grouping -->

</div><!-- End Accordion Definition -->
