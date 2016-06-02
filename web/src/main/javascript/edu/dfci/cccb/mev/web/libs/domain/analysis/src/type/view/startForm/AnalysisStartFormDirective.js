"use strict";
define(["./AnalysisStartForm.tpl.html", "./AnalysisStartForm.less"], function(template){
	function mevAnalsysStartFormDirective(){
		return {
			restrict: "EAC",
			scope: {
				analysisType: "=mevAnalysisType"
			},
			template: template,
			controller: ["$scope", function(scope){;
			}],
			link: function(scope, elm, attr, ctrl){
				scope.start=function(){
					console.debug("scope.analysisType", scope.analysisType, elm, attr);
					var postData = scope.analysisType.params.getValues();
					scope.errors = scope.analysisType.validate(postData);
					scope.isValid = scope.errors.length===0;
					if(scope.isValid) {
						scope.analysisType.start();
						elm.parents(".modal").modal("hide")
					}
					return scope.isValid;
					// scope.dataset.analysis.post({
					// 	datasetName : scope.dataset.datasetName,
					// 	analysisType : "topgo"
					// }, postData);
				}
			}
		};
	}
	mevAnalsysStartFormDirective.$inject=[];
	mevAnalsysStartFormDirective.$name="mevAnalysisStartFormDirective";
	return mevAnalsysStartFormDirective;
});