define(["ng"], function(ng){
	var PanelVM = function($scope){
		var isOpen;
		$scope.panel={				
				helloCtrl: "heyyyyy"
		};
	};
	PanelVM.$injcet=["$scope"];
	return PanelVM;
});