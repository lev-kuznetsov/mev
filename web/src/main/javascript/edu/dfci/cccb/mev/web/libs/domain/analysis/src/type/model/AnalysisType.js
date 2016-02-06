"use strict";
define(["lodash", "./AnalysisTypes"], function(_){
	var injectable = function(AnalysisLauncher, mevAnalysisTypes, AnalysisParamsFactory){

		function AnalysisType(id, name, params){
			// if(!spec.id)
			// 	throw new Error("id is required for AnalysisType");
			// if(!spec.params)
			// 	throw new Error("params is required for AnalysisType");

			if(arguments.length === 1)
				_.extend(this, arguments[0]);
			else
				_.extend(this, {id: id, name: name, params: params});
			mevAnalysisTypes.register(this);
		}
		_.extend(AnalysisType.prototype, { 
			start: function(){
				return AnalysisLauncher.start(this);
			}, 
			validate: function(){			
				return _.reduce(this.params, function(errors, param, key){

					if(_.isFunction(param.validate)){
						var error = param.validate();
						if(error)
							erros.push(error);
					}
					return errors;
				}, []);
			}
		});

		return AnalysisType;
		// return Object.create({}, {
		// 	id: {writable: true, enumerable: true, configurable: false, get: function(){return this.id}, set: function(v){this.id=v}}
		// 	id: {writable: true, enumerable: true, configurable: false, get: function(){return this.id}, set: function(v){this.id=v}}
		// })

	};
	injectable.$inject=["mevAnalysisLauncher", "mevAnalysisTypes", "mevAnalysisParams"];
	injectable.$name="mevAnalysisType";
	injectable.$provider="factory";
	return injectable;
});