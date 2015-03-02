define(["ng"], function(ng){
	return function DatasetListDirective($scope, $attr, $elem){
		return {
			scope: {
				showAdd: '@',
				showEdit: '@',
				isMenu: '@'
			},
			restrict: "AE",
			replace: true,
//			template: "<div>dataset list {{attr.showadd}}</div>",
			templateUrl: "app/widgets/dataset/datasetList/templates/datasetList.tpl.html",
			controller: "DatasetListVM",
			controllerAs: "DatasetListVM",
			link: function($scope, $elem, $attr){				
				$scope.attr=$attr;	
				if($attr.ismenu){
					$elem.addClass("dropdown-menu");
				}
			}
		}
	}
});
