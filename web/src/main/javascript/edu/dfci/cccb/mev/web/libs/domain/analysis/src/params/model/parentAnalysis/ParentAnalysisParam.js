define(["lodash", "../select/SelectParam", "../BaseParam"], function(_, SelectParam, BaseParam){
	"use strict";
	function ParentAnalysisParamFactory(mevAnalysisLocator){
		function ParentAnalysisParam(spec){

			_.assign(this, new SelectParam(
				_.assign(this, spec, {
						type: "select",
						options: mevAnalysisLocator.find.bind(this, spec.type),
						setValue: function(options){
							var analyses = options;
							if(analyses.length===1){
								this.value = analyses[0];								
							}
						}.bind(this),
						display: "name"				
					})
				)
			);
		}
		ParentAnalysisParam.prototype = new BaseParam();
		return ParentAnalysisParam;
	}
	ParentAnalysisParamFactory.$name="mevParentAnalysisParam";
	ParentAnalysisParamFactory.$provider="factory";
	ParentAnalysisParamFactory.$inject=["mevAnalysisLocator"];
	return ParentAnalysisParamFactory;
});