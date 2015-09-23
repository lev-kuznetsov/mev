define(["ng"], function(ng){
	var DashboardDirective = function DashboardDirective(setTransscope){
		
		return {
			restrict: "E",
//			replace: true,
			transclude: true,
			scope: {
				id: "@",
				hStretchItems: "="
			},
			template:	"<div  ng-class=\"{'container-flex': true, 'v-stretch': true, 'h-stretch': dashboard.hStretchItems}\" ng-transscope>" +					
//							"<mui-dashboard-item ng-repeat=\"item in dashboard.items\" name=\"{{item.name}}\" title=\"{{item.title}}\" content-width=\"item.contentWidth\" content-height=\"item.contentHeight\">{{item.content}}</mui-dashboard-item>" +
						"</div>",
			compile: function(){				
				return {
					pre: setTransscope,
					post: function(scope, elm, attr, controller, $transclude){		
						console.debug("dashboard link");
						controller.setAttr({hStretchItems: scope.hStretchItems});										
					}
				};
			},
			controller: "DashboardVM",
			controllerAs: "dashboard"
		};
	};
	DashboardDirective.$inject=["setTransscope"];
	DashboardDirective.$name="muiDashboardDirective";
	return DashboardDirective;
});