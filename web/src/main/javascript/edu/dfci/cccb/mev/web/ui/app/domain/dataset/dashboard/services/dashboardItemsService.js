define([], function(){
	var DashboardItemsValue = function DashboardItemsValue(){
		return [{
			name: "Original Data",
			templateUrl: "app/views/dataset/_templates/dataset.heatmap.tpl.html",
			viewModel: "DatasetHeatmapVMFactory"
		}];
	};
	DashboardItemsValue.$name="DashboardItems";
	DashboardItemsValue.provider="service";	
	DashboardItemsValue.$inject=[];
	return DashboardItemsValue;
});