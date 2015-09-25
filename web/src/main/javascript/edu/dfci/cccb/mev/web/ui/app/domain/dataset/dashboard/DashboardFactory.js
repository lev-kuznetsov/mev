define([], function(){
	var DashboardFactory = function DashboardFactory(){
		return function(){			
			var _self = this;
			this.items = {
					"Original Data": {				
						name: "Original Data",
						templateUrl: "app/views/dataset/_templates/dataset.heatmap.tpl.html",
						viewModel: "DatasetHeatmapVMFactory"
					}
			};
			this.add = function(item){
				_self.items[item.name] = item;
			};
		}
	};
	DashboardFactory.$name="DashboardFactory";	
	DashboardFactory.$inject=[];
	return DashboardFactory;
});