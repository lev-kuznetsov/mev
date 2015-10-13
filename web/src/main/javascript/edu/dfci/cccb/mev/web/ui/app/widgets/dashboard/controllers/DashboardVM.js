define(["ng", "lodash"], function(ng, _){
	"use strict";
	var DashboardVM = function DashboardVM($scope, $rootScope){
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
//			_.remove(_self.items, {name: target});
			delete _self.items[target];
			console.debug("remove _self.items",  _self.items);
		};
		this.items = $scope.dashboardItems;
			
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
			if(exists){
				$rootScope.$broadcast("ui:analysisLog.append", "info", "Cannot add analysis '" + data.name + "' to the dashboard. It is already there.");
			}else{
				_self.items.$add(data);
			}
		});
		$scope.$on("ui:dashboard:removeItem", function($event, data){
			console.debug("on ui:dashboard:removeItem", $event, data);
			_self.remove(data.name);	
		});
	};
	DashboardVM.$inject=["$scope", "$rootScope"];
	DashboardVM.$name="DashboardVMController";
	return DashboardVM;
});