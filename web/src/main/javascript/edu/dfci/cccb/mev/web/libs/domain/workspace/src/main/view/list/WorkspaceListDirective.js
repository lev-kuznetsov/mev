define(["./WorkspaceList.tpl.html", "./WorkspaceList.less"], function(tempalte){
    var directive = function(mevWorkspace, DatasetResource, mevAnnotationRepository){
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
                scope.vm={
                    activate: function(dataset){
                        DatasetResource.activate(dataset);
                    }
                };
                scope.$on("mev:datasets:list:refreshed", function(){
                    updateDatasetList();
                });
                updateDatasetList();
                scope.$on("mev:dataset:activated", function(event, dataset) {
                    var row = new mevAnnotationRepository("row");
                    row.import(dataset.id, "row");
                    var column = new mevAnnotationRepository("column");
                    column.import(dataset.id, "column");
                });
            }
        }
    }
    directive.$name = "mevWorkspaceList";
    directive.$provider = "directive";
    directive.$inject=["mevWorkspace", "DatasetResourceService", "mevAnnotationRepository"];
    return directive;
});