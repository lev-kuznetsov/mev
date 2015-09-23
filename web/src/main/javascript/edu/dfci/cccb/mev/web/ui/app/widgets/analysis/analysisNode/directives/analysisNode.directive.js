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
						return _.find(DashboardItems, {name: scope.node.nodeData.name}) ? true : false;
					}
				};
			}
		};
	};
	AnalysisNode.$name="AnalysisNodeDirective";
	AnalysisNode.$inject=["$rootScope", "$state", "DashboardItems"];
	return AnalysisNode;
});