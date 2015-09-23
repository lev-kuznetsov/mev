define(["ng"], function(ng){
	var DashbaordItemDirective = function DashbaordItemDirective($http){
		return {			
			restrict: "E",
			replace: true,
			transclude: true,
			require: "^muiDashboard",
			scope: {
				name: "@",
				contentWidth: "@",
				contentHeight: "@",
				contentTemplate: "@",
				muiOptions: "="
					
			},
			templateUrl: "app/widgets/dashboard/directives/dashboardItem.tpl.html",
			link: function(scope, elm, attr, controller, $transclude){				
				scope.panel={
						name: attr.name,						
						header: {
							title: attr.title
						},
						content: {
							height: attr.contentHeight,
							width: attr.contentWidth
						},
						max: function(){
							elm.siblings().hide();							
							controller.updateOptions({hStretchItems: true});
							scope.panel.isMax=true;
						},
						min: function(){
							controller.resetOptions();
							elm.siblings().show();
							scope.panel.isMax=false;
						},
						rowMax: function(){
							scope.panel.isRowMax=true;
						},
						rowMin: function(){
							scope.panel.isRowMax=false;
						},
						remove: function($event){
							console.debug("panel.remove", $event, scope.panel.name);
							if(scope.panel.isMax)
								scope.panel.min();
							controller.remove(scope.panel.name);
							elm.remove();
						}
				};
				
				if(scope.muiOptions){										
					ng.extend(scope.panel, scope.muiOptions);
					console.debug("muiDashboardItem options", scope.muiOptions, scope, attr, scope.panel);					
				}
				scope.$on("ui:dashboard:removeItem", function($event, data){
					console.debug("on panel ui:dashboard:removeItem", $event, data);
					if(attr.name === data.name)
						scope.panel.remove(data.name);
				});
				elm.find(".content > *").width(attr.contentWidth).height(attr.contentHeight);				
			}
		};
	};
	
	DashbaordItemDirective.$inject=["$http"];
	DashbaordItemDirective.$name="muiDashboardItem";
	DashbaordItemDirective.provider="Directive";
	
	return DashbaordItemDirective;
});