define(["./AnalysisStartForm.tpl.html", "./AnalysisStartForm.less"], function(template){"use strict";
	function mevAnalsysStartFormDirective($modal, $compile){
		return {
			restrict: "EAC",
			scope: {
				analysisType: "=mevAnalysisType"
			},
			template: template,
			controller: ["$scope", function(scope){;
			}],
			link: function(scope, elm, attr, ctrl){
				if(scope.analysisType.info && scope.analysisType.info.template){
					var elInfo = elm.find(".help-container");
					elInfo.html(scope.analysisType.info.template);
					$compile(elInfo.contents())(scope);
				}
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
				scope.info=function(){
					$modal.open({
						template: scope.analysisType.info.template,
						controller: ["$scope", function(scope, selectionSet){
							scope.dismiss = function() {
								scope.$dismiss();
							};
						}]
					}).result.finally(function(){
						// alert('done');
					});
				}
			}
		};
	}
	mevAnalsysStartFormDirective.$inject=["$uibModal", "$compile"];
	mevAnalsysStartFormDirective.$name="mevAnalysisStartFormDirective";
	return mevAnalsysStartFormDirective;
});