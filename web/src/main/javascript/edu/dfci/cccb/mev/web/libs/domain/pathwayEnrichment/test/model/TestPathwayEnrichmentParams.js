"use strict";
define(["mevPathwayEnrichment", "steal-jasmine", "mui", "angular-mocks"], 
function(mevPathwayEnrichment, jasmineRequire, ng, ngMocks){	
	return describe("mevPathwayEnrichment tests", function(){
		beforeEach(module("ngMock"));

		var SelectionSetAggregator;
		beforeEach(function(){
			SelectionSetAggregator = function(){return [{name: "s1", x: 1},{name: "s2", x: 2}, {name: "s3", x: 3}]};
			module(function($provide){
				$provide.value("mevSelectionSetAggregator", SelectionSetAggregator);
			});
		});

		beforeEach(module(mevPathwayEnrichment.name));

		var PathwayEnrichmentParams;
		beforeEach(inject(function(_mevPathwayEnrichmentParams_){
			PathwayEnrichmentParams = _mevPathwayEnrichmentParams_;			
		}));


		it("ensure PathwayEnrichmentParams.getValues works with object initializer", function(){ 	
			console.debug("mevPathwayEnrichment", PathwayEnrichmentParams.getValues());  			
			expect(PathwayEnrichmentParams.getValues()).toEqual({ name: undefined, organism: 'human', pAdjustMethod: 'fdr', pvalueCutoff: 0.05, minGSSize: 20, genelist: undefined });
			expect(true).toBe(true);     								
		});		 			

	});
});
