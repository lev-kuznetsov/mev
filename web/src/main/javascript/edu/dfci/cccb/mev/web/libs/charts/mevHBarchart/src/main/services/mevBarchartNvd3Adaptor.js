define(["lodash"],function(_){
	var service = function mevBarchartNvd3Adaptor(){
	return function(config, input){

			var groups = [];
			var group = {
				"key": config.series,
				"color": "#1f77b4",
				"values": config.y.sort ? _.orderBy(input, function(item){
					return config.y.get(item);
				}, config.y.sort) : input
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