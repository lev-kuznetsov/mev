define(["mui", "angular-ui-router", "../project/MockProject"], function(ng){ "use strict";

	function config($stateProvider){	
		$stateProvider	   	     		
 		.state("mock", {	 			
 			template: "hiiii",
 			resolve:{
 				project: ["mevMockProject", function(mevMockProject){
 					return mevMockProject;
 				}]
 			}
 		})
 		.state("root", {
 			abstract: true,
 			url: "/",
 			template: "<ui-view></ui-view>",
			resolve: {
 				project: ["mevMockProject", function(mevMockProject){
 					return mevMockProject;
 				}]
 			}
 		})
 		.state("root.dataset", {
 			parent: "root",
 			url: "dataset/{datasetId}/",
 			abstract: true,
 			template: "<ui-view></ui-view>",
 			resolve: {
 				dataset: ["project", function(project){
 					return project.dataset;
 				}]
 			}
 		});	
	}
	config.$inject=["$stateProvider"];
	config.$provider="config";
	return config;

});