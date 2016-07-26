define(["lodash", "../BaseParam"], function(_, BaseParam){
	function SelectParam(spec){

		var options = spec.options;
		if(!_.isFunction(options)){			
			spec.options = function(){			
				return options;
			};
		}		
		_.assign(this, {type: "select"}, spec);
	};
	SelectParam.prototype = new BaseParam();
	return SelectParam;
});