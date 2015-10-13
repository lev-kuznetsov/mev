define(["ng"], function(ng){
	"use strict";
	var PresetsListDirective = function PresetsListDirective(TcgaPreset, PresetsListVM){
		return {			
			restrict: 'EA',
			templateUrl: 'app/widgets/presets/_templates/presetsList.tpl.html',
//			cotroller: 'PresetsListVM',
//			cotrollerAs: 'PresetsListVM',
			link: {
				pre: function($scope, $elm, $attrs, controller){
					//fetch data
					var presets = TcgaPreset.findAll();					
					$scope.PresetsListVM = new PresetsListVM(presets);						
				} 		
			}
		};
	};
	PresetsListDirective.$inject=['TcgaPreset', 'PresetsListVM'];
	return PresetsListDirective;
});