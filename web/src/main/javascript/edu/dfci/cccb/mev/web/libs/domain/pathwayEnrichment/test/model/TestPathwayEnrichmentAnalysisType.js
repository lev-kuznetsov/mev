"use strict";
define(["mevPathwayEnrichment", "mev-analysis", "steal-jasmine", "mui", "angular-mocks"], 
function(mevPathwayEnrichment, mevAnalysis, jasmineRequire, ng, ngMocks){
	describe("Test PathwayEnrichmentAnalsyisType", function(){
		beforeEach(module("ngMock"));
		var SelectionSetAggregator;
		beforeEach(function(){
			SelectionSetAggregator = function(){return [{name: "s1", x: 1},{name: "s2", x: 2}, {name: "s3", x: 3}];};
			module(function($provide){
				$provide.value("mevSelectionSetAggregator", SelectionSetAggregator);
			});
		});
		var AnalysisLauncher;
		beforeEach(function(){
			AnalysisLauncher = {
				start: function(analysisType){
					console.log("started " + analysisType.id, analysisType);
				}
			};
			module(function($provide){
				$provide.value("mevAnalysisLauncher", AnalysisLauncher);
			});
		});
		beforeEach(module(mevPathwayEnrichment.name));
		beforeEach(module(mevAnalysis.name));

		var peAnalysisType;
		beforeEach(inject(function(_mevPathwayEnrichmentAnalysisType_){
			peAnalysisType = _mevPathwayEnrichmentAnalysisType_;
		}));
		var mevAnalysisTypes;
		beforeEach(inject(function(_mevAnalysisTypes_){
			mevAnalysisTypes = _mevAnalysisTypes_;
		}));

		it("Should test PathwayEnrichmentAnalysisType defaults", function(){
			console.debug("peAnalysisType", peAnalysisType);
			console.debug("mevAnalysisTypes", mevAnalysisTypes);
			expect(peAnalysisType.id).toBe("pe");
			expect(peAnalysisType.name).toBe("Pathway Enrichment");
			expect(peAnalysisType.params.length).toBe(6);
			expect(mevAnalysisTypes.get("pe")).toBe(peAnalysisType);
		});
	});
});