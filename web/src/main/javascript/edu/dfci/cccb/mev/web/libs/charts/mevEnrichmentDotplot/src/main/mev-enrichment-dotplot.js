define(["mui", "jquery", "d3", "nvd3", "angular-nvd3", "lodash", 
	"./view/mevEnrichmentDotplotDirective",
	"./services/mevDotplotNvd3Adaptor",
	"./services/mevEnrichmentDataAdaptor",
	"nvd3/build/nv.d3.css",
	"./style/mevEnrichmentDotplot.less"], function(ng){
	return ng.module("mevEnrichmentDotplot", arguments, arguments);
});