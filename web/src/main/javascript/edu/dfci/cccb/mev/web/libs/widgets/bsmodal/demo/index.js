"use strict";
define(["mui", "mev-bs-modal", "mev-bs-modal/src/view/modal/BsModalDirective", "mev-bs-modal/src/view/trigger/BsModalTriggerDirective",
	"bootstrap", "bootstrap/dist/css/bootstrap.min.css"], function(ng){
	var demo = ng.module("demo", arguments, arguments);

	ng.element(document).ready(function(){
		ng.bootstrap(document, [demo.name]);
	});
});