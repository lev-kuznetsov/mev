define(["mui", "jquery", "d3", "nvd3", "angular-nvd3", "lodash", 
	"./view/mevEnrichmentBarchartDirective",
	"./services/mevBarchartNvd3Adaptor",
	"./services/mevEnrichmentDataAdaptor",
	"nvd3/build/nv.d3.css",
	"./style/mevEnrichmentBarchart.less"], function(ng){
	return ng.module("mevEnrichmentBarchart", arguments, arguments);
});