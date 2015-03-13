define(["ng"], function(ng){	
	var YankDirective = function(){
		return {
			restrict: "A",
			
			link: function(scope, elem, attrs, ctrl){
    			var targetElement = ng.element("body [mui-paste='"+attrs.muiYank+"']");
    			console.log("YANK targetElement", attrs.muiYank, targetElement);
    			
    			if(targetElement.length===0)
    				throw "Cannot find matching paste element for yank " + attrs.muiYank;
    			
    			if(targetElement.length===1){
    				targetElement.html('');
    				console.debug("YANK remove", attrs.muiYank, targetElement);
    				
    				console.debug("YANK appaned", attrs.muiYank, targetElement);
					targetElement.append(elem);
    			}    			
    		}
		};
	};
	YankDirective.$inject=[];
	return YankDirective;
});