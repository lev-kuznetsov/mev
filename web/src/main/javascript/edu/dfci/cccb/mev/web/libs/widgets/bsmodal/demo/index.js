"use strict";
define(["mui", "mev-bs-modal", 
	"bootstrap", "bootstrap/dist/css/bootstrap.min.css"], function(ng){
	var demo = ng.module("demo", arguments, arguments);

	ng.element(document).ready(function(){
		ng.bootstrap(document, [demo.name]);
	});
});