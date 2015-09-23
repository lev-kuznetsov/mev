define([], function(){
	var transcludePointDirective = function transcludePointDirective(){
		return {
			priority: 1000,
			link: function(scope, elm, attr, ctrl, $transclude){
				console.debug("transcludePoint", elm);
				elm.append($transclude());
			}			
		};
	};
	transcludePointDirective.$inject=[];
	transcludePointDirective.$name="transcludePointDirective";
	return transcludePointDirective;	
});