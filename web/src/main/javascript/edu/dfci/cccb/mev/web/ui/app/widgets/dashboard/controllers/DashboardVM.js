define(["ng", "lodash"], function(ng, _){
	var DashboardVM = function DashboardVM($scope, DashboardItems, $rootScope){
		var _self = this;				
		this.toggleHStretch = function(isOn){				
			_self.setOptions(options);
		};
		this.updateOptions = function(options){
			ng.extend(_self, options);
		};
		this.resetOptions = function(){
			_self.updateOptions(_self.attr);
		};
		this.setAttr = function(attr){
			_self.attr=attr;
			_self.updateOptions(attr);
		};				
		this.remove = function(target){
			_.remove(_self.items, function(item){
				if(typeof target === "string")
					return item.name === target;
				else
					return item == target;
			});
			console.debug("remove _self.items", name, _self.items);
		};
		this.items = DashboardItems;
//		this.items = [
//	        {
//	        	name: "survival1",
//	        	title: "survival1",
//	        	content: "moving around"
//	        },
//			{
//				name: "hcl1",
//				title: "hcl1",
//				content: "moving around"
//			},
//			{
//				name: "ttest1",
//				title: "ttest1",
//				content: "moving around"
//			},
//			{
//				name: "limma1",
//				title: "limma1",
//				content: "moving around"
//			},
//			{
//				name: "deseq1",
//				title: "deseq1",
//				content: "moving around"
//			},
//			{
//				name: "nmf",
//				title: "nmf",
//				content: "moving around"
//			},
//			{
//				name: "kmeans1",
//				title: "kmeans1",
//				content: "moving around"
//			}
//			];		
		$scope.$on("ui:dashboard:addItem", function($event, data){
			var exists = _.find(_self.items, {name: data.name});
			if(!exists){
				_self.items.push(data);
			}else{
				$rootScope.$broadcast("ui:analysisLog.append", "info", "Cannot add analysis '" + data.name + "' to the dashboard. It is already there.");
			}
		});
		$scope.$on("ui:dashboard:removeItem", function($event, data){
			console.debug("on ui:dashboard:removeItem", $event, data);
			_self.remove(data.name);
		});
	};
	DashboardVM.$inject=["$scope", "DashboardItems", "$rootScope"];
	DashboardVM.$name="DashboardVMController";
	return DashboardVM;
});