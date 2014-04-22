var dependencies = [
	'heatmap/Behaviors/cellFilter',
	'heatmap/Behaviors/viewUpdate'
	];

define(dependencies, function(
			cellFilter,
			viewUpdate){
	
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