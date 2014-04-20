var dependencies = [
	'heatmap/Behaviors/datasetViewFilter',
	'heatmap/Behaviors/setHeatmapView',
	'heatmap/Behaviors/setHeatmapViewToDefault',
	'heatmap/Behaviors/addNewSelection',
	'heatmap/Behaviors/clearAnalysisList',
	'heatmap/Behaviors/setDataset',
	'heatmap/Behaviors/setDatasetAnalysisList',
	'heatmap/Behaviors/setDatasetSelections',
	'heatmap/Behaviors/addToAnalysisList',
	'heatmap/Behaviors/addToPanel'
	];

define(dependencies, function(
			datasetViewFilter, 
			setHeatmapView, 
			setHeatmapViewToDefault, 
			addNewSelection,
			clearAnalysisList,
			setDataset,
			setDatasetAnalysisList,
			setDatasetSelections,
			addToAnalysisList,
			addToPanel){
	
	//The loader for heatmap dependencies
	var behaviors = Object.create(null);
	
	dependencies.map(function(behaviorName){
		Object.defineProperty(behaviors, behaviorName.split('/')[2], {
			value:eval(behaviorName.split('/')[2]),
			configurable: false,
			enumerable:true,
			writable:false
			});
	});
	
	return behaviors;
	
});