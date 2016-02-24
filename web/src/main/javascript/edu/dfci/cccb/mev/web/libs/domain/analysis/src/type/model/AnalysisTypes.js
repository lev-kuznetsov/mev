"use strict";
define([], function(){
	function mevAnalysisTypes(){
		var analysisTypes = {};

		function registerType(spec){
			if(!spec.id){
				throw "Error: no id provided for analysisType: " + JSON.stringify(spec);
			}
			if(analysisTypes[spec.id]){				
				throw new Error("Type with id '" + spec.id + "' is already registered")
			}
												 
			analysisTypes[spec.id] = spec;
		}

		function getType(id){
			return analysisTypes[id];			
		}
		function getAll(){
			return analysisTypes;
		}

		return {
			register: registerType,	
			get: getType,
			all: getAll
		};		
		// return {
		// 	$get: [function(type){
		// 		if(analysisTypes[type])
		// 			return analysisTypes[type];

		// 		analysisTypes[type] = new 
		// 	}]
		// }

	}
	mevAnalysisTypes.$inject=[];
	mevAnalysisTypes.$name="mevAnalysisTypes";
	mevAnalysisTypes.$provider="service";
	return mevAnalysisTypes;
});