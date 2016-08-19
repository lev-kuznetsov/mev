define(["./presetsList.tpl.html"], function(template){
	"use strict";
	var PresetsListDirective = function PresetsListDirective(TcgaPreset, PresetsListVM){
		return {			
			restrict: 'EA',
			// templateUrl: 'app/widgets/presets/_templates/presetsList.tpl.html',
			template: template,
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
	PresetsListDirective.$name="presetsList";
	PresetsListDirective.$provider="directive";
	return PresetsListDirective;
});