define(["lodash", "../select/SelectParam", "../BaseParam"], function(_, SelectParam, BaseParam){
	"use strict";
	function AnnotationFieldParamFactory(mevAnnotationsLocator){
		function AnnotationFieldParam(spec){			
			_.assign(this, new SelectParam(
				_.assign(this, spec, {
						type: "select",
						options: function(){
							return mevAnnotationsLocator.find(spec.dimension).getFields();
						}
					})
				)
			);
		}
		AnnotationFieldParam.prototype = new BaseParam();
		return AnnotationFieldParam;
	}
	AnnotationFieldParamFactory.$name="mevAnnotationFieldParamFactory";
	AnnotationFieldParamFactory.$provider="factory";
	AnnotationFieldParamFactory.$inject=["mevAnnotationsLocator"];
	return AnnotationFieldParamFactory;
});