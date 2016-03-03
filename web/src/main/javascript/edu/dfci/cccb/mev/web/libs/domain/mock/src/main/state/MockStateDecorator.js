"use strict";
define(["mui", "angular-ui-router", "../project/MockProject"], function(ng){

	function config($delegate, mevMockProject){
		$delegate.$current.locals.globals.project = mevMockProject;		
		return $delegate;
	}

	// function config($stateProvider){	
	// 	$stateProvider	   	     		
 // 		.state("mock", {	 			
 // 			resolve:{
 // 				project: ["mevMockProject", function(mevMockProject){
 // 					return mevMockProject;
 // 				}]
 // 			}
 // 		});	
	// }

	// config.$inject=["$stateProvider"];
	config.$inject=["$delegate", "mevMockProject"];
	config.$name="$state";
	config.$provider="decorator";
	return config;

});