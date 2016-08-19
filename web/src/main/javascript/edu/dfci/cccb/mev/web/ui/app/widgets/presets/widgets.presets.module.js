define(["mui", "../../domain/presets/domain.presets.module", "./_templates/PresetsListVM", "./_templates/presetsListDirective"],
function(ng, PresetsListVM, PresetsListDirective){
	var module = ng.module("mui.widgets.presets", ["mui.domain.presets.tcga"], arguments);
	return module;
});
