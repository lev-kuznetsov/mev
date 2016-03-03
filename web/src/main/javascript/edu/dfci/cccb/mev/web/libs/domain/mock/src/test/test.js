"use strict";
define(["mev-mock", "mev-domain-common", "steal-jasmine", "angular-mocks", "angular-ui-router"], function(mevMock, mevCommon){
	describe("Test Mock Project", function(){

		beforeEach(module("ngMock"));
		beforeEach(module("ui.router"));		
		beforeEach(module(mevMock.name));
		beforeEach(module(mevCommon.name));
		var $rootScope;
		beforeEach(inject(function(_$rootScope_){
			$rootScope = _$rootScope_;
		}));

		var mevMockProject;
		beforeEach(inject(function(_mevMockProject_){
			mevMockProject = _mevMockProject_;
		}));

		it("Should have a mock project", function(){				

			expect(mevMockProject.name).toBe("mouse_test_data.tsv");
			expect(mevMockProject.dataset.id).toBe("mouse_test_data.tsv");
		});

		var $state;
		beforeEach(inject(function(_$state_){
			$state = _$state_;			
		}));
		it("Should get mock state", function(){		
			$state.go("mock");
			$rootScope.$digest();
			console.log("$state", $state);			
			expect($state.current.name).toBe("mock");
		});

		var mevContext;
		beforeEach(inject(function(_mevContext_){
			mevContext = _mevContext_;
		}))
		it("Should get project from mock state", function(){		
			$state.go("mock");						
			$rootScope.$digest();
			expect(mevContext.root().name).toBe("mouse_test_data.tsv");
			expect(mevContext.root().dataset.id).toBe("mouse_test_data.tsv");
		});		
	});
});