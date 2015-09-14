define(["ng", 
        "./limma/widgets.analysis.limma.module", 
        "./hcl/widgets.analysis.hcl.module", 
        "./survival/widgets.analysis.survival.module",
        "./default/widgets.analysis.default.module",
        "./analysisModal/widgets.analysis.modal.module",
        "./analysisLog/widgets.analysis.log.module",
        "./topgo/widgets.analysis.topgo.module"
        ], 
function(ng, AnalyisEventBus){
	var module = ng.module("mui.widgets.analysis", ["mui.widgets.analysis.limma",
	                                                "mui.widgets.analysis.hcl",
	                                                "mui.widgets.analysis.survival",
	                                                "mui.widgets.analysis.default",
	                                                "mui.widgets.analysis.modal",
	                                                "mui.widgets.analysis.log",
	                                                "mui.widgets.analysis.topgo"
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
			"Anova Analysis": {
				shortName: "anova",
				viewModel: "AnovaVM"	
			},
			"DESeq Differential Expression Analysis": {
				shortName: "deseq",
				viewModel: "DESeqVM"
			},
			"Survival Analysis": {
				shortName: "survival",
				viewModel: "SurvivalVM"
			},
			"Non-Negative Matrix Factorization":{
				shortName: "nmf",
				viewModel: "NmfVM"
			},
			"topGO Analysis":{
			    shortName: "topgo",
			    viewModel: "TopGoVM"
			},
			reverseLookup: {
				hcl: "Hierarchical Clustering",
				limma: "LIMMA Differential Expression Analysis",
				kmeans: "K-means Clustering",
				one_sample_ttest: "t-Test Analysis",
				two_sample_ttest: "t-Test Analysis",
				fisher: "Fisher test",
				anova: "Anova Analysis",
				deseq: "DESeq Differential Expression Analysis",
				nmf: "Non-Negative Matrix Factorization",
				survival: "Survival Analysis",
				topgo: "topGO Analysis"
		} 
	});	
	return module;
});
