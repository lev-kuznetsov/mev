define(["ng"], function(ng){
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

	};
	DatasetHomeVM.$inject=["$scope", "$stateParams", "$state", "dataset", "project"];
	return DatasetHomeVM;
});
