//Make sure global is set. blob@0.0.4 expects it to be there.
window.global = window;
define(["mui", "jquery", "./app/app", './less/boot.less'], function(ng, jquery, app){		
	ng.element(document).ready(function(){
		ng.bootstrap(document, [app.name]);
	});
	window.OpenRefineBridge = OpenRefineBridge;	
});