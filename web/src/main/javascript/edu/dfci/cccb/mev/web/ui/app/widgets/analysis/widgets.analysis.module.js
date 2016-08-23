define(["ng",
        "./default/widgets.analysis.default.module",
        "./analysisModal/widgets.analysis.modal.module",
        "./analysisLog/widgets.analysis.log.module",
        "./analysisMenu/widgets.analysis.menu.module",
        "./any/widgets.analysis.any.module",
        "./analysisNode/widgets.analysis.analysisNode.module",
        "./genesd/widgets.analysis.genesd.module",
        "./histogram/widgets.analysis.histogram.module"
        ], 
function(ng, AnalyisEventBus){
	var module = ng.module("mui.widgets.analysis", arguments, arguments)
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
			"TopGO Analysis":{
			    shortName: "topgo",
			    viewModel: "TopGoVM"
			},
			"Histogram Analysis":{
			    shortName: "histogram",
			    viewModel: "HistogramVM"
			},
			"Gene SD Analysis":{
				shortName: "genesd",
				viewModel: "GeneSDVM"
			},
			"Gene MAD Analysis":{
				shortName: "genemad",
				viewModel: "GeneMADVM"
			},
			"voom":{
				shortName: "voom",
				viewModel: "VoomVM"
			},
			// "pca":{
			// 	shortName: "pca",
			// 	viewModel: "PcaVM"
			// },
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
				topgo: "TopGO Analysis",
				histogram: "Histogram Analysis",
				genesd: "Gene SD Analysis",
				genemad: "Gene MAD Analysis",
				voom: "voom",
				pca: "pca",
		} 
	});	
	return module;
});