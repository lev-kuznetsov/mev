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
	                                                ])
	.constant("AnalysisTypes", {
			"Hierarchical Clustering": {
				shortName: "hcl",
				viewModel: "HclVM"
			},
			"LIMMA Differential Expression Analysis": {
				shortName: "limma",
				viewModel: "LimmaVM"
			},
			"K-means Clustering": {
				shortName: "kmeans",
				viewModel: "KMeansVM"
			},
			"t-Test Analysis": {
				shortName: "ttest",
				viewModel: "tTestVM"
			},
			"Fisher test":{
				shortName: "fisher",
				viewModel: "FisherVM"
			},
			reverseLookup: {
				hcl: "Hierarchical Clustering",
				limma: "LIMMA Differential Expression Analysis",
				kmeans: "K-means Clustering",
				one_sample_ttest: "t-Test Analysis",
				two_sample_ttest: "t-Test Analysis",
				fisher: "Fisher test"
		} 
	});	
	return module;
});