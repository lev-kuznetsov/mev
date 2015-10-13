define(["ng"], function(ng){	
	var YankDirective = function(){
		return {
			restrict: "A",
			
			link: function(scope, elem, attrs, ctrl){
				scope.$on("mui:utils:yank", function($event, targetId, sourceElement){
					var targetElement = elem;					
					console.log("PASTE targetElement", attrs.muiPaste, targetId, sourceElement);
					if(attrs.muiPaste === targetId){																								
						
						targetElement.html('');
						console.debug("PASTE remove", attrs.muiYank, targetElement);
						
						console.debug("PASTE appaned", attrs.muiYank, targetElement);
						targetElement.append(sourceElement);
						
						$event.stopPropagation();
					}
				});
    		}
		};
	};
	YankDirective.$inject=[];
	YankDirective.$name="muiPasteDirective";
	return YankDirective;
});	