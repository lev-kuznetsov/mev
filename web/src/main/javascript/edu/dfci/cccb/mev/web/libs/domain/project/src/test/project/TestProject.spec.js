"use strict";
define(["lodash", "mev-project/data/mouse_test_data.tsv.json", "steal-jasmine", "mui", "angular-mocks", "mev-project"], 
function(_, mouseJson){
	describe("Test Dataset", function(){
		beforeEach(module("ngMock"));
		beforeEach(module("mevProject"));

		var ProjectFactory;
		beforeEach(inject(function(_mevProject_){
			ProjectFactory = _mevProject_;
		}))

		it("should create a dataset", function(){			
			var id = "mouse_test_data.tsv";
			var project = ProjectFactory(id, mouseJson);
			console.log("mouseJson", mouseJson, project);
			expect(project.name).toBe(id);
			expect(project.dataset.id).toBe(id)
		});
	});
});