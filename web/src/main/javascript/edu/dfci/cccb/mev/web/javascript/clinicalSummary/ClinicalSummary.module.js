define(["angular", "angularResource", "angularRoute", "bootstrap", "jqueryUi"], function(angular, ngResource, ngRoute){
	console.debug("init angular module Mev.ClinicalSummary");
	var module = angular.module("Mev.ClinicalSummary", ["ngResource", "ngRoute"]);
	module.path="/container/javascript/clinicalSummary/";
	return module;
		
});