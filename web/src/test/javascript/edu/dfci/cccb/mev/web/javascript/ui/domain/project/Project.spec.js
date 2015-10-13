define(["ng", "lodash", "ngMocks"], function(ng, _){
	"use strict";
	describe("Project domain model", function(){
		
		beforeEach(module("ngMock"));
		beforeEach(module("js-data"));
		beforeEach(module("mui.domain"));		
		
		var $httpBackend, DS;
		beforeEach(inject(function(_DS_, _$httpBackend_){
			$httpBackend=_$httpBackend_;
			DS=_DS_;
		}));
		
		var ProjectResource, DatasetResource;
		beforeEach(inject(function(_MevProject_, _MevDataset_){
			ProjectResource=_MevProject_;
			DatasetResource=_MevDataset_;
		}));
		
		var mockResponse = function(mock){
			return function(method, url, data){
				return [200, mock, {}];
			}
		}
		
		var mockProject, mockDatasets, mockDatasetsNoForeignKey;
		beforeEach(function(){
			mockProject = {name: "Katya"};
			mockDatasets  = [{name: "smile", projectName: "Katya"}, {name: "frown", projectName: "Katya"}, {name: "chomp", projectName: "Olifka"}];
			mockDatasetsNoForeignKey  = [{name: "smile"}, {name: "frown" }, {name: "chomp"}];
		});
		
		it("Should retrieve a project", function(){
			$httpBackend.expectGET(/project\/Katya\?format=json/).respond(mockResponse(mockProject));
			ProjectResource.find("Katya").then(function(project){				
				expect(project.name).toBe("Katya");
				console.debug("project0", project);
			});
			$httpBackend.flush();
		});
		
		it("Should retrieve a full project", function(){
//			mockProject.datasets = _.filter(mockDatasets, {projectName: "Katya"});
			mockProject.datasets = mockDatasetsNoForeignKey;
			$httpBackend.expectGET(/project\/Katya\?format=json/).respond(mockResponse(mockProject));
			ProjectResource.find("Katya").then(function(project){				
				expect(project.name).toBe("Katya");
				expect(project.datasets.length).toBe(3);
				console.debug("project.datasets", project.datasets);
				expect(project.datasets[2].projectName).toBe("Katya");
				expect(project.datasets[2].project.name).toBe("Katya");
				console.debug("project0", project);
			});
			$httpBackend.flush();
		});
		
		it("Should retrieve a project with datasets", function(){
			$httpBackend.expectGET(/project\/Katya\?format=json/).respond(mockResponse(mockProject));
			$httpBackend.expectGET("/project/Katya/dataset?format=json").respond(mockResponse(mockDatasets));
			
			ProjectResource.find("Katya").then(function(project){				
				expect(project.name).toBe("Katya");
				console.debug("project1", project);
				return ProjectResource.loadRelations(project, ["Dataset"]);				
			}).then(function(project){
				expect(project.datasets.length).toBe(2);
				expect(project.datasets[0].name).toBe("smile");
				console.debug("project2", project);
			});
			$httpBackend.flush(2);
		});
		
		
		afterEach(function(){
			DS.clear;
			$httpBackend.verifyNoOutstandingExpectation();
			$httpBackend.verifyNoOutstandingRequest();			
		});
	});
});