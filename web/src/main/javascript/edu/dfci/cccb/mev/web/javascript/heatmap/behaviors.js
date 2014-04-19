var dependencies = ['heatmap/Behaviors/datasetViewFilter'];

define(dependencies, function(datasetViewFilter){
	
	//The loader for heatmap dependencies
	var behaviors = Object.create(null)
	
	dependencies.map(function(behaviorName){
		Object.defineProperty(behaviors, behaviorName.split('/')[2], {
			value:eval(behaviorName.split('/')[2])
			})
	})
	
	return behaviors
	
});