"use strict";
define(["mui", "angular-mocks", "mev-analysis"], function(ng, ngMocks, mevAnalysis){

	describe("Test AnalysisTypes", function(){
		
		var AnalysisType, AnalysisTypes, AnalysisLauncher;
		beforeEach(module("ngMock"));
		beforeEach(module(mevAnalysis.name));
		beforeEach(function (){
			AnalysisLauncher =  {
				start: function(params){
					return "started " + params.name;
				}
			};
			module(function($provide){
				$provide.value("mevAnalysisLauncher", AnalysisLauncher);
			});
		});
		beforeEach(inject(function(_mevAnalysisType_, _mevAnalysisTypes_){
			AnalysisType = _mevAnalysisType_;
			AnalysisTypes = _mevAnalysisTypes_;
		}));

		it("ensures AnalysisTypes.register works", function(){
			var mockType = new AnalysisType("mockAnalsysisType",  "Mock Analysis", {
				getValues: function(){
					return { name: "my analysis"};
				}
			});			
			expect(AnalysisTypes.get("mockAnalsysisType")).toEqual(mockType);
			spyOn(AnalysisLauncher, "start");
			mockType.start()
			expect(AnalysisLauncher.start).toHaveBeenCalled();			
		});

		it("ensures AnalysisType.start works", function(){
			var mockType = new AnalysisType("mockAnalsysisType",  "Mock Analysis", {
				getValues: function(){
					return { name: "my analysis"};
				}
			});			
			expect(AnalysisTypes.get("mockAnalsysisType")).toEqual(mockType);
			spyOn(AnalysisLauncher, "start");
			mockType.start()
			expect(AnalysisLauncher.start).toHaveBeenCalled();			
		});
		
		it("ensures analysisTypes.all works", function(){
			var mockType = {id: "mockAnalsysisType"};
			AnalysisTypes.register(mockType);
			expect(AnalysisTypes.get("mockAnalsysisType")).toBe(mockType);
			
			var mockType2 = {id: "mockAnalsysisType2"};
			AnalysisTypes.register(mockType2);
			expect(AnalysisTypes.all().mockAnalsysisType).toBe(mockType);
			expect(AnalysisTypes.all().mockAnalsysisType2).toBe(mockType2);

		});

		it("ensures analysisTypes are unique by id", function(){
			var mockType = {id: "mockAnalsysisType"};
			AnalysisTypes.register(mockType);
			expect(AnalysisTypes.get("mockAnalsysisType")).toBe(mockType);
			
			var mockType2 = {id: "mockAnalsysisType"};
			
			expect(function(){ 
				AnalysisTypes.register(mockType2); 
			}).toThrow(new Error("Type with id 'mockAnalsysisType' is already registered"));
			
		});

	});

});