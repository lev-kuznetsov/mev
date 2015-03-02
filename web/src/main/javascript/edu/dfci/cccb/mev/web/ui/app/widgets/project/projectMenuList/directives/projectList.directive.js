define(["ng"], function(ng){
	return function ProjectListDirective($scope, $attr, $elem){
		return {
			scope: {
				showAdd: '@',
				showEdit: '@'
			},
			restrict: "AE",
			replace: true,
//			template: "<div>project list {{attr.showadd}}</div>",
			templateUrl: "app/widgets/project/projectList/templates/projectList.tpl.html",
			controller: "ProjectListVM",
			controllerAs: "ProjectListVM",
			link: function($scope, $elem, $attr){			
				$scope.attr=$attr;				
			}
		}
	}
});
