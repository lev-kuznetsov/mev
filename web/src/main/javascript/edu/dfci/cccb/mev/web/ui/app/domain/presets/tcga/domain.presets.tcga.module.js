define(["mui", "./model/TcgaPreset", "js-data", "js-data-angular"],
function(ng, TcgaPreset){
	var module = ng.module("mui.domain.presets.tcga", arguments, arguments);
	module.config(function(DSProvider, DSHttpAdapterProvider){
//		ng.extend(DSProvider.defaults, { });
		ng.extend(DSHttpAdapterProvider.defaults, {
			basePath: "/",
			httpConfig: {
				params: {
					format: "json"
				}
			}
		});
	});
	// module.factory("TcgaPreset", TcgaPreset);
	return module;
});