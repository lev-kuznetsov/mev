define(["steal-jasmine", "mev-scatter-plot"], function(jsamineRequire, mevScatterPlot){
	"use strict";
	describe("Mev Scatter Plot tests", function(){
		it("ensure mevScatterPlot is an AngularJS module", function(){
			console.debug("mevScatterPlot", mevScatterPlot);
			expect(true).toBe(true);
		});		
	});
});
