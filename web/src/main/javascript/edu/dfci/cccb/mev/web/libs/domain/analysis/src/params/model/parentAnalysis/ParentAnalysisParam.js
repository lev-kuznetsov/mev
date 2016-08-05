define(["lodash", "../select/SelectParam", "../BaseParam"], function(_, SelectParam, BaseParam){
	"use strict";
	function ParentAnalysisParamFactory(mevAnalysisLocator){
		function ParentAnalysisParam(spec){

			_.assign(this, spec, {
						type: "select",
						options: function(type){
							var analyses = mevAnalysisLocator.find.call(this, type); 
							return _.isArray(analyses) 
								? analyses.map(function(analysis){
										return {name: analysis.name};
									})
								: analyses; 
						}.bind(this, spec.type),
						setValue: function(options){
							var analyses = options;
							if(analyses.length===1){
								this.value = analyses[0];								
							}	
						}.bind(this),
						display: "name"				
					});
		}
		ParentAnalysisParam.prototype = new SelectParam();
		return ParentAnalysisParam;
	}
	ParentAnalysisParamFactory.$name="mevParentAnalysisParam";
	ParentAnalysisParamFactory.$provider="factory";
	ParentAnalysisParamFactory.$inject=["mevAnalysisLocator"];
	return ParentAnalysisParamFactory;
});