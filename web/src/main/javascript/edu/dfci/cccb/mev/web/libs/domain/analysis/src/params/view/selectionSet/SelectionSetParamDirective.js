"use strict";
define(["lodash", "mev-analysis/src/params/model/select/SelectParam"], function(_, SelectParam){
	function SelectionSetParamDirective(){
		return {

			restrict: "EAC",
			template: '<div class="mev-select-param" mev-param="selectParam"></div>',
			scope: {
				param: "=mevParam",
				dataset: "=mevDataset"
			},
			controller: ["$scope", function($scope){
				this.selections = $scope.dataset[$scope.param.dimension].selections;
				this.options = Object.values($scope.param)[0];
				this.key = Object.keys($scope.param)[0];
				this.selectParam[this.key] = new SelectParam(
					_.assign({}, this.options, {
						displayName: this.options.dimension + " set",
						options: this.selection
					})
				);
			}],
			controllerAs: "SelectionSetParamVM"
		};
	}
	SelectionSetParamDirective.$inject=[];
	SelectionSetParamDirective.$name="SelectionSetParamDirective";
	return SelectionSetParamDirective;
});