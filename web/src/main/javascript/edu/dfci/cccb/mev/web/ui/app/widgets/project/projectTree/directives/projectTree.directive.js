define(["jquery"], function($){
	return function ProjectTree(){
		
		
		return {
			scope: {
				project: '=',
				nodeType: '@'
			},			
			restrict: "AE",
//			templateUrl: "app/widgets/project/projectTree/templates/projectTree.tpl.html",			
//			templateUrl: "app/widgets/project/projectTree/templates/projectTree.dxTree.tpl.html",
			templateUrl: "app/widgets/project/projectTree/templates/projectTree.bootstrapTree.tpl.html",
			controller: "ProjectTreeVM",
			controllerAs: "ProjectTreeVM",
			link: {
				post: function(scope){
					console.debug("**scope", scope);
//					scope.initBootstrapTree=initBootstrapTree;
//					scope.bootstrapTreeClick=bootstrapTreeClick.bind(scope);
//					scope.vm={visible: true};
				}
			}
			
		}
	}
});