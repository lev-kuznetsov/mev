define(["ng",
        "./survival/domain.analsyis.survival.module",
        "./topgo/domain.analsyis.topgo.module",
        "./pca/domain.analysis.pca.module], 
function(ng){
	var module = ng.module("mui.domain.analysis",
	                       [ "mui.domain.analysis.survival",
	                         "mui.domain.analysis.topgo",
	                         "mui.domain.analysis.pca" ]);
	return module;
});