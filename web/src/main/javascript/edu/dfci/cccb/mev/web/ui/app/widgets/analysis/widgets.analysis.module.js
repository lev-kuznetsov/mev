define(["ng", 
        "./limma/widgets.analysis.limma.module", 
        "./hcl/widgets.analysis.hcl.module", 
        "./default/widgets.analysis.default.module",
        "./analysisModal/widgets.analysis.modal.module",
        "./analysisLog/widgets.analysis.log.module"
        ], 
function(ng, AnalyisEventBus){
	var module = ng.module("mui.widgets.analysis", ["mui.widgets.analysis.limma",
	                                                "mui.widgets.analysis.hcl",
	                                                "mui.widgets.analysis.default",
	                                                "mui.widgets.analysis.modal",
	                                                "mui.widgets.analysis.log"
	                                                ]);
	return module;
});