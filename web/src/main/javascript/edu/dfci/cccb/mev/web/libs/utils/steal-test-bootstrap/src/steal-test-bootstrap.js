define(["steal-jasmine", "live-reload"], function(jasmineRequire, reload){
	"use strict";
	reload(function(){
		window.location.reload(); 
		// jasmine.getEnv();
	});
	reload("*", function(name, suite){
		// window.location.reload(); 
		// console.log("spec", "spec=mevPathwayEnrichment tests");
		// if(name.indexOf("@")>0)
		// 	window.location.search="spec="+suite.description;
		// window.location.search="spec=mevPathwayEnrichment tests"
		// jasmine.getEnv();
	}); 

	return describe("Bootstrap tests", function(){
		it("ensure tests can run", function(){
			console.debug("reload", reload);
			expect(true).toBe(true);	
		});		
	});
});
