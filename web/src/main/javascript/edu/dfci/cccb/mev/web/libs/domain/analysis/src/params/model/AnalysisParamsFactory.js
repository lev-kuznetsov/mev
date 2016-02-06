"use strict";
define(["lodash", "./name/NameParam"], function(_, NameParam){
	


	function AnalysisParamsFactory(){	
		return function mevAnalysisParamsFactory(params, validate){
			var nameParam = new NameParam({
				required: true
			});		
			if(_.isArray(params)){
				params.unshift(nameParam);

				params.getValues = function(){				
					var result = {};
					return _.reduce(this, function(result, item, key){					
						result[item.id]=item.value;
						return result;
					}, result);				
				};				
				params.validate = validate;
				return params;
			}	
			else{
				var tmp = Object.create({
					getValues: function(){				
						return _.mapValues(this, function(o){					
							return o.value;
						});				
					},
					validate: validate
				});
				tmp[nameParam.id]=nameParam;
				var ret = _.assign(tmp, params);						
				console.log("ret", ret);
				_.map(ret, function(o, key){
					o.id=o.id || key;
				});		
				return ret;
			}				
		}		
	}

	AnalysisParamsFactory.$inject=[];
	AnalysisParamsFactory.$name="mevAnalysisParams";
	AnalysisParamsFactory.$provider="factory";
	return AnalysisParamsFactory;
});	