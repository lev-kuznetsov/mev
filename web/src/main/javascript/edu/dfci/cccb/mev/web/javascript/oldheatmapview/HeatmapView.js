define([
	'./lib/defaultParams',
	'./lib/setScales',
	'./lib/viewCellFilter'], 
	function(defaultParams, setScales, viewCellFilter){

	//The loader for heatmap dependencies
	var behaviors = Object.create(null);
	
		
	Object.defineProperty(behaviors, 'params', {
		value:defaultParams,
		configurable: false,
		enumerable:true,
		writable:false
	});
	
	Object.defineProperty(behaviors, 'setScales', {
		value:setScales,
		configurable: false,
		enumerable:true,
		writable:false
	});
	
	Object.defineProperty(behaviors, 'viewCellFilter', {
		value:viewCellFilter,
		configurable: false,
		enumerable:true,
		writable:false
	});
		
	
	return behaviors;
	
});