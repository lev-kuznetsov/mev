define(["ng"], function(ng){
	var DatasetHeatmapVMFactory=function (){
		return function DatasetHeatmapVMFactory($scope, project){
			var _self=this;
			this.project=project;
			this.dataset=project.dataset;
			console.debug("DatasetHeatmapVMFactory", project);
			console.debug("***dataset home", project);
//		this.annotations=annotations;	
			
			this.heatmapView = project.generateView({
				viewType:'heatmapView',
				note: "dataset.heatmap",
				labels:{
					row:{keys:_self.dataset.row.keys}, 
					column:{keys:_self.dataset.column.keys}
				},
				expression:{
					min: _self.dataset.expression.min,
					max: _self.dataset.expression.max,
					avg: _self.dataset.expression.avg,
				}
			});			
		};
	};
	DatasetHeatmapVMFactory.$inject=[];
	DatasetHeatmapVMFactory.$name="DatasetHeatmapVMFactory";
	DatasetHeatmapVMFactory.$provider="factory";
	return DatasetHeatmapVMFactory;
});
