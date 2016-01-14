define(["ng", "jquery", "app/app", 'orefine/OrefineBridge', './less/boot.less'], function(ng, jquery, app, OpenRefineBridge){		
	ng.element(document).ready(function(){
		ng.bootstrap(document, [app.name]);
	});
	window.OpenRefineBridge = OpenRefineBridge;
});