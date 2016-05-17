define(["mui", "lodash", "angular-color-picker",
	"./views.dataset.columnSets.edit.tpl.html", "angular-color-picker/angular-color-picker.css"],

function(ng, _, ngColorPicker,
		 template, colorPickerCss){
	
	var module=ng.module("mui.views.dataset.columnSets.edit", arguments, arguments);
	
	module.config(['$stateProvider',
		function($stateProvider) {

			$stateProvider.state("root.dataset.columnSets.edit", {
					url: "/edit/{name}",
					parent: "root.dataset.columnSets",
					params: {
						setId: undefined
					},
					resolve: {
						selectionSet: ["$stateParams", "dataset", function($stateParams, dataset){
							var reset = dataset.resetSelections("column");
							console.debug("resolve ColumnSet", reset);

							return reset.$promise.then(function(){
								var columnSet = _.find(dataset.column.selections, function(selection){
									return selection.name === $stateParams.setId;
								});

								if(!columnSet){
									throw new Error("Selection Set '" + $stateParams.setId + "' not found" );
								}

								console.debug("resolved ColumnSet", columnSet);
								return columnSet;
							});
						}]
					},
					onEnter: ["$stateParams", "$state", "$uibModal", "selectionSet", "dataset", "dimension",
						function($stateParams, $state, $modal, selectionSet, dataset, dimension){
						$modal.open({
							template: template,
							resolve: {
								selectionSet: ["$stateParams", function ($stateParams) {
									return selectionSet;
								}],
							},
							controller: ["$scope", "selectionSet", function(scope, selectionSet){
								scope.selectionSet = _.cloneDeep(selectionSet);
								scope.dismiss = function() {
									scope.$dismiss();
								};
								scope.save = function() {
									_.assign(selectionSet, scope.selectionSet);
									dataset.selection.put({
										datasetName: dataset.datasetName,
										dimension: dimension
									}, selectionSet,
									function (response) {
										dataset.resetSelections(dimension);
									});
									scope.$close(true);
								};
							}]
						}).result.finally(function(){
							$state.go("^");
						});
					}]
				});
		}]);
	return module;
});