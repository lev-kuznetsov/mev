"use strict";
define([], function(){
	function state($stateProvider){
		$stateProvider	   	     					
	 		.state("root.dataset.analysisType", {	 			
	 			parent: "root.dataset",
	 			url: "analysisType/",
	 			abstract: true,
	 			template: "<ui-view></ui-view>",	   	     			
	 			resolve:{
	 				anaysisType: [function(){
	 					console.log("pe hi");
	 				}]
	 			}
	 		});
	}
	state.inject=["$stateProvider"];
	state.provider="config";
	return state;
});