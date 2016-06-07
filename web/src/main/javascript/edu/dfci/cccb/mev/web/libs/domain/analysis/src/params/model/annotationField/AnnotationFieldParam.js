define(["lodash", "../select/SelectParam", "../BaseParam"], function(_, SelectParam, BaseParam){
	"use strict";
	function AnnotationFieldParamFactory(mevAnnotationsLocator){
		function AnnotationFieldParam(spec){			
			_.assign(this, new SelectParam(
				_.assign(this, spec, {
						type: "select",
						options: function(){
							return mevAnnotationsLocator.find(spec.dimension).getFields().then(function(options){
								return _.isArray(options)
									? options.map(function(option){
										return option.name;
									})
									: undefined;
							});
						},
						refreshListeners: ["openRefine:loadedAnnotations:"+spec.dimension]
					})
				)
			);
		}
		AnnotationFieldParam.prototype = new BaseParam();
		return AnnotationFieldParam;
	}
	AnnotationFieldParamFactory.$name="mevAnnotationFieldParam";
	AnnotationFieldParamFactory.$provider="factory";
	AnnotationFieldParamFactory.$inject=["mevAnnotationsLocator"];
	return AnnotationFieldParamFactory;
});