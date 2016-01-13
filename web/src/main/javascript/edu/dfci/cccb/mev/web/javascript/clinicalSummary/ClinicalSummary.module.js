define(["angular", "angular-route", "angular-route", "bootstrap", "jquery-ui"], function(angular, ngResource, ngRoute){
	var module = angular.module("Mev.ClinicalSummary", ["ngResource", "ngRoute"]);
	module.path="/container/javascript/clinicalSummary/";
	return module;
		
});