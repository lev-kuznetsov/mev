define(["lodash"],function(_){
	var service = function mevDotplotNvd3Adaptor(){
	return function(config, input){
			var groups = [];
			var sortfunc = config.series.sort && config.series.sort.field ?
				_.isFunction(config.series.sort.field) ?
					config.series.sort.field :
					function(item){
						return item[config.series.sort.field];
					}
				: undefined;
			var group = {
				"key": config.series.label,
				"color": "#1f77b4",
				"values": sortfunc ? _.orderBy(input, sortfunc, config.series.sort.order) : input
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