//Make sure global is set. blob@0.0.4 expects it to be there.
"use strict";
window.global = window;
import angular from "mui";
import jquery from "jquery";
import app from "./app/app";
import boot from "./less/boot.less";
import reload from "live-reload";
import liver from "liver";

var root = angular.element(document);
root.ready(function(){	
	if(root.injector()){			
		root.scope().$root.$destroy();
		root.data("$injector", null);
		angular.element("body > ui-view").removeData().empty();		
		console.log("ng already bootstrapped, force re-init", root.injector());
	}
	angular.bootstrap(root, [app.name]);    
});

reload(function(){		  
	console.log("reload");
});
reload("*", function(name, modx){		  
	console.log("reload", name, modx);
});


// define(["ng", "jquery", "./app/app", './less/boot.less'], function(ng, jquery, app){		
// 	ng.element(document).ready(function(){
// 		ng.bootstrap(document, [app.name]);
// 	});
// 	window.OpenRefineBridge = OpenRefineBridge;	
// });