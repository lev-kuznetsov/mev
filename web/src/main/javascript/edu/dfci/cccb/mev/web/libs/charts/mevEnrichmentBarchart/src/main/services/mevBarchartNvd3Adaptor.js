define(["lodash"],function(_){
	var service = function mevBarchartNvd3Adaptor(){
	return function(config, input){
			var groups = [];
			var group = {
				"key": config.series,
				"color": "#1f77b4",
				"values": _.sortBy(input, function(item){
					return config.y.field ? _.isFunction(item[config.y.field]) ? -item[config.y.field]() : -item[config.y.field] : -item.getMatched();
				})
			};
			groups.push(group);			
			return groups;
		};
	};
	service.$name="mevBarchartNvd3Adaptor";
	service.$provider="factory";
	service.$inject=[];
	return service;
});