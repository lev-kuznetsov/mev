<accordion-group heading="K-Means Clustering: {{analysis.name}}">

	Clusters: {{analysis.clusters.length}}
	
	<div ng-repeat="cluster in analysis.clusters">
		Cluster: {{cluster.id}}
		Length: {{cluster.length}}
	
	</div>
	
	<button class="btn btn-success" ng-click="updateHeatmapView(analysis)">Apply </button>
	<button class="btn btn-success pull-right" >
        <a href="/dataset/{{datasetName}}/analysis/{{analysis.name}}?format=tsv">
          <i class="icon-white icon-download"></i> Download
        </a> 
    </button>
</accordion-group>