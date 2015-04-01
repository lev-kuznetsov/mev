define(["ng"], function(ng){
	var ProjectNodeDefaultDirective = function ProjectNodeDefaultDirective (){
		return {
			restrict: "AE",
			scope: {
				node: "="
			},			
			templateUrl: "app/widgets/project/projectTree/_projectNode/templates/projectNodeDefault.bootstrapTree.tpl.html",
//			templateUrl: "app/widgets/project/projectTree/_projectNode/templates/projectNodeDefault.tpl.html"
			link: function(scope, elem, attr){
				scope.isObject=ng.isObject;
				scope.vm={
						onClick: function(){
							console.debug("DefaultNode.click", attr, scope.node);
						}
				};				
			}
		};
	}; 
	ProjectNodeDefaultDirective.$inject=[];
	return ProjectNodeDefaultDirective;
});