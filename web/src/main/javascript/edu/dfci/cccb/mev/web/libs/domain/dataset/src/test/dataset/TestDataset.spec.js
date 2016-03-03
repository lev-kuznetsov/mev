"use strict";
define(["lodash", "mev-dataset/data/mouse_test_data.tsv.json", "steal-jasmine", "mui", "angular-mocks", "mev-dataset"], 
function(_, mouseJson){
	describe("Test Dataset", function(){
		beforeEach(module("ngMock"));
		beforeEach(module("mevDataset"));

		var DatasetFactory;
		beforeEach(inject(function(_DatasetFactory_){
			DatasetFactory = _DatasetFactory_;
		}))

		it("should create a dataset", function(){
			var dataset = DatasetFactory("mouse_test_data.tsv", mouseJson);
			console.log("mouseJson", mouseJson, dataset);
			expect(dataset.id).toBe("mouse_test_data.tsv");
		});
	});
});