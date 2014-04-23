define([
	'./lib/cellFilter',
	'./lib/viewUpdate'
	], function(cellFilter, viewUpdate){

	
	//The loader for heatmap dependencies
	var behaviors = Object.create(null);
	
	Object.defineProperty(behaviors, 'cellFilter', {
		value: cellFilter,
		enumerable: true,
		writable: false,
		configurable: false
	});
	
	Object.defineProperty(behaviors, 'viewUpdate', {
		value: viewUpdate,
		enumerable: true,
		writable: false,
		configurable: false
	});
	
	return behaviors;
	
});