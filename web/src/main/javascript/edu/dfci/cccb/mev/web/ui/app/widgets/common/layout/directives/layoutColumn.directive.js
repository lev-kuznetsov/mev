define(["ng"], function(ng){
	var LayoutColumnDirective = function($rootScope, LayoutSrv, $animate){
		
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
				//the $animate:close event does not fire for ccs-driven annomatinos
//				elem.bind("$animate:close",
//                        function handleAnimationEvent( event, data ) {
//                            console.debug("ANIMATE2 ... DONE");
//                        });
				//addClass and removeClass do not fire for the column-layout container,
				//instead does fire for the div:flex-spacing container (it has an ng-class attribute) and it's children link elements ("a") those that have ng-hide/ng-show attributes set
				$animate.on("addClass", elem, function(element, phase){
					console.debug("ANIMATE3 ... ", phase, elem, element.context.className);
//					if(phase==="close")
//						scope.$broadcast("ui:layoutColumn:sizeChanged");
				});
				$animate.on("removeClass", elem, function(element, phase){
					console.debug("ANIMATE4 ... ", phase, elem, element.context.className);
//					if(phase==="close")
//						scope.$broadcast("ui:layoutColumn:sizeChanged");
				});
			}
		};
	};
	
	LayoutColumnDirective.$inject=["$rootScope", "LayoutSrv", "$animate"];
	return LayoutColumnDirective;  
});