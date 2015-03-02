define(["ng"], function(ng){
	return function ListMenuDirective(){
		return {
			scope: {
				label: "@"
			},
			restrict: "E",
//			template: "<div>ListMenuDirective</div>",
			transclude: true,
			replace: true,
			templateUrl: "app/widgets/common/listMenu/listMenu.tpl.html",
			controller: "ListMenuVM",
			controllerAs: "ListMenuVM",
			link: function($scope, $elem, $attr, controller, transclude){
				$elem.find('ng-transclude').replaceWith(transclude());
//				DashboardRepository.getAll().then(function(items){
//					$scope.vm.items=items;	
//				});
//				$scope.$state=$state;
			}
		}
	}
});