define(["./WorkspaceList.tpl.html", "./WorkspaceList.less"], function(tempalte){
    var directive = function(mevWorkspace, mevAnnotationRepository, $timeout){
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
                        dataset.status = "activating";
                        mevWorkspace.activateDataset(dataset);
                    },
                    delete: function(datasetId){
                        if(confirm("Delete dataset '"+datasetId+"'?"))
                            return mevWorkspace.deleteDataset(datasetId)
                                .then(updateDatasetList);
                    },
                    deactivate: function(datasetId){
                        if(confirm("Deactivate dataset '"+datasetId+"'?"))
                            return mevWorkspace.deactivate(datasetId)
                                .then(updateDatasetList);
                    },
                    showStatus: function(row){
                        var status = row.getStatus();
                        return status;
                    },
                    isSaved: function(row){
                        return row.getStatus()==="saved";
                    },
                    export: function(dataset){
                        this.exportStatus[dataset.id] = true;
                        mevWorkspace.exportDataset(dataset);
                    },
                    exportStatus: {},
                    getExportStatus: function(datasetId){
                        return this.exportStatus[datasetId];
                    }
                };
                scope.$on("mev:datasets:list:refreshed", function(){
                    updateDatasetList();
                });
                updateDatasetList();
                scope.$on("mev:dataset:activated", function(event, dataset) {
                    dataset.status = "active";
                    var row = new mevAnnotationRepository("row");
                    row.import(dataset.id, "row");
                    var column = new mevAnnotationRepository("column");
                    column.import(dataset.id, "column");
                });
                scope.$on("mev:dataset:exported", function(event, dataset){
                    $timeout(function(){
                        scope.vm.exportStatus[dataset.id] = false;
                    });
                });
            }
        }
    }
    directive.$name = "mevWorkspaceList";
    directive.$provider = "directive";
    directive.$inject=["mevWorkspace", "mevAnnotationRepository", "$timeout"];
    return directive;
});