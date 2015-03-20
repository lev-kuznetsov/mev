define(["ng"], function(ng){
	var LayoutColumnDirective = function($rootScope, LayoutSrv){
		
		return {
			restrict: "A",					
			require: ["^layoutRow", "layoutColumn"],
			controller: "LayoutColumnVM",
			controllerAs: "layoutColumn",
//			template: "<div>HAAHA</div>",
			link: function(scope, elem, attrs, controller){
				
				
				
				console.debug("LayoutColumnDirective.link controller", controller);
				var isClosed=false;
				
				layoutColumn={
					hide: function(){
						console.debug("HIDE", elem);
						isClosed=true;
					},
					show: function(){
						isClosed=true;
					},
					toggle: function(){
//						console.debug("layoutColumn.toggle", attrs.layoutColumn, isClosed, controller[0].rand, controller[1].rand);
						$rootScope.$broadcast("ui:layoutColumn:toggle", {position: self.position});						
					},
					isClosed: function(){
//						console.debug("layoutColumn.isClosed", attrs.layoutColumn, isClosed);
						return isClosed;
					},
					position: attrs.layoutColumn					
				};
				
				ng.extend(controller[1], layoutColumn);
				LayoutSrv.register(layoutColumn);
				scope.$on("ui:layoutColumn:toggle", function(event, args){					
						isClosed=!isClosed;
						console.debug("ui:layoutColumn:toggle", isClosed);
//						isClosed=false;
//					}
				});
			}
		};
	};
	
	LayoutColumnDirective.$inject=["$rootScope", "LayoutSrv"];
	return LayoutColumnDirective;  
});