<accordion-group heading="K-Means Clustering: {{analysis.name}}">

	Clusters: {{analysis.clusters.length}}
	
	<div ng-repeat="cluster in analysis.clusters">
		Cluster: {{cluster.id}}
		Length: {{cluster.length}}
	
	</div>
	
	<button class="btn btn-success" ng-click="updateHeatmapView(analysis)">Apply </button>
	
</accordion-group>