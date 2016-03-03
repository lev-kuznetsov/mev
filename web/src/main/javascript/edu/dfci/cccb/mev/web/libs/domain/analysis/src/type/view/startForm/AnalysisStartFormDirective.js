"use strict";
define(["./AnalysisStartForm.tpl.html"], function(template){
	function mevAnalsysStartFormDirective(){
		return {
			restrict: "EAC",
			scope: {
				analysisType: "=mevAnalysisType"
			},
			template: template,
			controller: ["$scope", function(scope){
				scope.start=function(){ 
					console.debug("scope.analysisType", scope.analysisType);
					var postData = scope.analysisType.params.getValues();
					scope.errors = scope.analysisType.validate();
					scope.isValid = scope.errors.length===0;
					if(scope.isValid)
						scope.analysisType.start();
					return scope.isValid;
					// scope.dataset.analysis.post({
        			// 	datasetName : scope.dataset.datasetName, 
        			// 	analysisType : "topgo"
        			// }, postData);
				};
			}]
		};
	}
	mevAnalsysStartFormDirective.$inject=[];
	mevAnalsysStartFormDirective.$name="mevAnalysisStartFormDirective";
	return mevAnalsysStartFormDirective;
});