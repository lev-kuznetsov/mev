define(["ng", "lodash"], function(ng, _){
	var DatasetHomeVM=function DatasetViewVM($scope, $stateParams, $state, dataset, project){
		that=this;
		this.project=project;
		this.dataset=dataset;
		console.debug("DatasetHomeVM", dataset, project);
		console.debug("***dataset home", dataset, project);
//		this.annotations=annotations;	
		
		this.heatmapView = project.generateView({
            viewType:'heatmapView',
            note: "dataset.home",
            labels:{
                row:{keys:dataset.row.keys}, 
                column:{keys:dataset.column.keys}
            },
            expression:{
                min: dataset.expression.min,
                max: dataset.expression.max,
                avg: dataset.expression.avg,
            }
        });
        this.getDownloadFileName = function(){
            return _.endsWith(project.dataset.datasetName, ".tsv")
                ? project.dataset.datasetName
                : project.dataset.datasetName + ".tsv";
        }

	};
    DatasetHomeVM.$name="DatasetHomeVM";
    DatasetHomeVM.$provider="controller";
	DatasetHomeVM.$inject=["$scope", "$stateParams", "$state", "dataset", "project"];
	return DatasetHomeVM;
});
