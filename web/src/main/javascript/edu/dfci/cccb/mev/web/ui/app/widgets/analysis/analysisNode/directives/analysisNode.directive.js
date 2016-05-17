define(["lodash"], function(_){
	var AnalysisNode = function($rootScope, $state, DashboardItems){
		return {
			scope: {
				node: '='				
			},			
			restrict: "AE",
			templateUrl: "app/widgets/analysis/analysisNode/directives/analysisNode.tpl.html",			
//			controller: "AnalysisNodeVM",
//			controllerAs: "AnalysisNodeVM",
			link: function(scope, elm, attrs, ctrl){
				console.debug("AnalysisNode link", scope.node);				
				scope.AnalysisNodeVM={
					onClick: function(e){
						console.debug("AnalysisNode onClick", e);
//						$rootScope.$state.go("root.dataset.AnalysisSet", {setId: "new"});
						$rootScope.$broadcast("ui:dashboard:addItem", {name: scope.node.nodeData.name});						
						e.stopPropagation();
					},
					isDashboardState: function(){
						return $state.includes("root.dataset.home");
					},
					existsInDashboard: function(){
						return _.find(new DashboardItems(), {name: scope.node.nodeData.name}) ? true : false;
					},
					getDisplayName: function(){
						if(scope.node.parent)
							return scope.node.nodeData.name.replace(scope.node.parent.nodeData.name+".", "");
						return scope.node.nodeData.name;
					},
					delete: function(e){
						console.debug("selectionSetNode onClick", e);
						if(confirm("Delete analysis '"+scope.node.nodeData.name+"'?"))
							$rootScope.$broadcast("root.dataset.analysis.delete", scope.node.nodeData);
						e.stopPropagation();
					}
				};
			}
		};
	};
	AnalysisNode.$name="AnalysisNodeDirective";
	AnalysisNode.$inject=["$rootScope", "$state", "DashboardItems"];
	return AnalysisNode;
});