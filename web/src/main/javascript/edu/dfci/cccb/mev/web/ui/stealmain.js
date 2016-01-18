//Make sure global is set. blob@0.0.4 expects it to be there.
window.global = window;
define(["ng", "jquery", "./app/app", './less/boot.less'], function(ng, jquery, app, OpenRefineBridge){		
	ng.element(document).ready(function(){
		ng.bootstrap(document, [app.name]);
	});
	window.OpenRefineBridge = OpenRefineBridge;	
});