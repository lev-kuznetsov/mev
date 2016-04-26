define(["mui", "jquery", "d3", "nvd3", "angular-nvd3", "lodash", 
	"./view/mevDotplotDirective", 
	"./services/mevDotplotNvd3Adaptor",	
	"./services/mevDotplotDataAdaptor",	
	"nvd3/build/nv.d3.css",
	"./style/mevDotplot.less"
	], function(ng){
	return ng.module("mev-dotplot", arguments, arguments);
});
