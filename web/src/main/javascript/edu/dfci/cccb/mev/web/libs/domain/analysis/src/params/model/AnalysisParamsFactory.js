define(["lodash", "./name/NameParam"], function(_, NameParam){ "use strict";
	function AnalysisParamsFactory(){	
		return function mevAnalysisParamsFactory(params, validate){
			var nameParam = new NameParam({
				required: true
			});		
			if(_.isArray(params)){
				if(!_.find(params, function(item){return item.id==="name"})){
					params.unshift(nameParam);
				}

				params.getValues = function(){				
					var result = {};
					return _.reduce(this, function(result, item, key){
						if(item.checkConstraint())
							result[item.id] = item.getValue
								? item.getValue()
								: (item.value && item.bound
									? item.value[item.bound]
									: item.value);
						return result;
					}, result);				
				};
				_.forEach(params, function(param){
					param.params = params;
				})
				params.validate = validate;
				params.getById = function(id){
					return _.find(this, function(param){
						return param.id===id;
					});
				};
				return params;
			}	
			else{
				var tmp = Object.create({
					getValues: function(){				
						return _.mapValues(this, function(o){					
							return o.getValue ? o.getValue() : o.value;
						});				
					},
					validate: validate,
					getById: function(id){
						return this[id];
					}
				});
				tmp[nameParam.id]=nameParam;
				var ret = _.assign(tmp, params);						
				console.log("ret", ret);
				_.map(ret, function(o, key){
					o.id=o.id || key;
				});		
				return ret;
			}				
		};
	}

	AnalysisParamsFactory.$inject=[];
	AnalysisParamsFactory.$name="mevAnalysisParams";
	AnalysisParamsFactory.$provider="factory";
	return AnalysisParamsFactory;
});	