define(["ng", "./_templates/PresetsListVM", "./_templates/presetsListDirective"], 
function(ng, PresetsListVM, PresetsListDirective){
	var module = ng.module("mui.widgets.presets", ["mui.domain.presets.tcga"]);	
	module.factory("PresetsListVM", PresetsListVM);
	module.directive("presetsList", PresetsListDirective);
	return module;
});
