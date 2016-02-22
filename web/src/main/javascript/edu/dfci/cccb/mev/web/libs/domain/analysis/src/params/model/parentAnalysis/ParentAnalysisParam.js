define(["lodash", "../select/SelectParam", "../BaseParam"], function(_, SelectParam, BaseParam){
	"use strict";
	function ParentAnalysisParamFactory(mevAnalysisLocator){
		function ParentAnalysisParam(spec){

			_.assign(this, new SelectParam(
				_.assign(this, spec, {
						type: "select",
						options: mevAnalysisLocator.find.bind(this, spec.type)
					})
				)
			);
		}
		ParentAnalysisParam.prototype = new BaseParam();
		return ParentAnalysisParam;
	}
	ParentAnalysisParamFactory.$name="mevParentAnalysisParamFactory";
	ParentAnalysisParamFactory.$provider="factory";
	ParentAnalysisParamFactory.$inject=["mevAnalysisLocator"];
	return ParentAnalysisParamFactory;
});