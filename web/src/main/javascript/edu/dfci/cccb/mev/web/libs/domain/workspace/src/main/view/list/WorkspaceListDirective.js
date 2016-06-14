define(["./WorkspaceList.tpl.html"], function(tempalte){
    var directive = function(mevWorkspace){
        return {
            restrict: "EAC",
            template: tempalte,
            scope: {},
            link: function(scope, elm, attr, ctrl){
                function updateDatasetList(){
                    mevWorkspace.getDatasets().then(function(datasets){
                        scope.datasets = datasets;
                        scope.datasetsByStatus = {
                            active: scope.datasets.filter(function(dataset){
                                return dataset.isActive;
                            }),
                            inactive: scope.datasets.filter(function(dataset){
                                return !dataset.isActive;
                            })
                        }
                    });
                }
                scope.$on("mev:datasets:list:refreshed", function(){
                    updateDatasetList();
                });
                updateDatasetList();
            }
        }
    }
    directive.$name = "mevWorkspaceList";
    directive.$provider = "directive";
    directive.$inject=["mevWorkspace"];
    return directive;
});