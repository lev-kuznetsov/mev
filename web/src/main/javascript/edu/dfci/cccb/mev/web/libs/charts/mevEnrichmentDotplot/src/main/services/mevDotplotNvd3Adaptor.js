define(["lodash"],function(_){
	var service = function mevDotplotNvd3Adaptor(){
	return function(config, input){
			var groups = [];
			var group = {
				"key": config.series,
				"color": "#1f77b4",
				"values": _.sortBy(input, function(item){
					return -item.getMatched();
				})
			};
			groups.push(group);			
			return groups;
		};
	};
	service.$name="mevDotplotNvd3Adaptor";
	service.$provider="factory";
	service.$inject=[];
	return service;
});