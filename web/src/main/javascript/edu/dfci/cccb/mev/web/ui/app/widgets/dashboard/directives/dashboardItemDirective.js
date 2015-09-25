define(["ng"], function(ng){
	"use strict";
	var DashbaordItemDirective = function DashbaordItemDirective(DashboardLayout){
		
		function Panel(name, elm, attr, controller){
				var _self = this;				
				_self.name = attr.name,
				_self.elm = elm;
				_self.isMax = false;
				_self.isRowMax = true;
				_self.header = {
					title: attr.title
				}
				_self.content = {
					height: attr.contentHeight,
					width: attr.contentWidth
				};
				_self.max = function(){
					_self.elm.siblings().hide();							
					controller.updateOptions({hStretchItems: true});
					_self.isMax=true;
				};
				_self.min = function(){
					controller.resetOptions();
					_self.elm.siblings().show();
					_self.isMax=false;
				};
				_self.rowMax = function(){
					_self.isRowMax=true;
				};
				_self.rowMin = function(){
					_self.isRowMax=false;
				};
				_self.remove = function($event){
					console.debug("panel.remove", $event, _self.name);
					if(_self.isMax)
						_self.min();
					controller.remove(_self.name);
					delete DashboardLayout.panels[_self.name];
					elm.remove();
				};
		}
		
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
				if(!DashboardLayout.panels[attr.name]){					
					DashboardLayout.panels[attr.name] = new Panel(attr.name, elm, attr, controller);				
					if(scope.muiOptions){										
						ng.extend(DashboardLayout.panels[attr.name], scope.muiOptions);
						console.debug("muiDashboardItem options", scope.muiOptions, scope, attr, scope.panel);					
					}
				}			
				
				scope.panel =  DashboardLayout.panels[attr.name];
				scope.panel.elm = elm;
				scope.$on("ui:dashboard:removeItem", function($event, data){
					console.debug("on panel ui:dashboard:removeItem", $event, data);
					if(attr.name === data.name){						
						scope.panel.remove(data.name);
					}
				});
				elm.find(".content > *").width(attr.contentWidth).height(attr.contentHeight);				
			}
		};
	};
	
	DashbaordItemDirective.$inject=["DashboardLayout"];
	DashbaordItemDirective.$name="muiDashboardItem";
	DashbaordItemDirective.provider="Directive";
	
	return DashbaordItemDirective;
});