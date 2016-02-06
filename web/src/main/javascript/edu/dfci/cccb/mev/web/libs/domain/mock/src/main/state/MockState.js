"use strict";
define(["mui", "angular-ui-router", "../project/MockProject"], function(ng){

	function config($stateProvider){	
		$stateProvider	   	     		
 		.state("mock", {	 			
 			resolve:{
 				project: ["mevMockProject", function(mevMockProject){
 					return mevMockProject;
 				}]
 			}
 		});	
	}
	config.$inject=["$stateProvider"];
	config.$provider="config";
	return config;

});