define([], function(){
    var factory = function(){
        function SelectionSetEdit_OnEnter($state, $modal, selectionSet, dataset, dimension){
            $modal.open({
                template: "<selection-set-edit></selection-set-edit>",
                resolve: {
                    selectionSet: function () {
                        return selectionSet;
                    },
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
        }
        SelectionSetEdit_OnEnter.$inject=["$state", "$uibModal", "selectionSet", "dataset", "dimension"];
        return SelectionSetEdit_OnEnter;
    };
    factory.$name="SelectionSetsEditOnEnter";
    factory.$provider="factory";
    return factory;
});