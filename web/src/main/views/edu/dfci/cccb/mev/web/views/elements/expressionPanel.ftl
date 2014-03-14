

<tabset>
	<tab heading="Visualize" active="activeTab=='visualize'" ng-click="setActiveTab('visualize')">
		<vis-Heatmap></vis-Heatmap>
	</tab>

	<tab heading="Annotations" active="activeTab=='annotations'" ng-click="setActiveTab('annotations')">
		<!-- my-iframe id="annotationsIframe" height="100%" width="99%"></my-iframe -->
		<iframe scrolling="no" frameborder="0" width="99%" height="100%" ng-src="{{annotationsUrl}}" />
	</tab>
</tabset>