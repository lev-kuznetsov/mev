"use strict";
define(["steal-jasmine", "mui", "angular-mocks", "angular-ui-router", "mev-domain-common"], function(jasmineRequire, ng){
	return describe("Test Domain Common", function(){
		beforeEach(module("ngMock"));
		beforeEach(module("ui.router"));
		beforeEach(module("mevDomainCommon"));
		var project = {
			dataset: {
				selections: {
					"column":[{"name":"s2","properties":{"selectionColor":"#90ad43","selectionDescription":""},"keys":["SA10-06-25-14","SA11_06_25_14","SA15_06_25_14"],"type":"column","$$hashKey":"object:4007"},{"name":"s1","properties":{"selectionColor":"#f7a06","selectionDescription":""},"keys":["SA2_06_25_14","SA4_006_25_14","SA5_06_25_14","SA8_06_25_14"],"type":"column","$$hashKey":"object:4008"}],
					"row":[{"name":"r2","properties":{"selectionColor":"#90ad43","selectionDescription":""},"keys":["SA10-06-25-14","SA11_06_25_14","SA15_06_25_14"],"type":"column","$$hashKey":"object:4007"},{"name":"r1","properties":{"selectionColor":"#f7a06","selectionDescription":""},"keys":["SA2_06_25_14","SA4_006_25_14","SA5_06_25_14","SA8_06_25_14"],"type":"column","$$hashKey":"object:4008"}]									
				}
			}
		};
		ng.module("test", ["ui.router"])
		.config(["$stateProvider", function($stateProvider){
			$stateProvider
			.state("root", {
				abstract: true			
			})
			.state("root.dataset", {					
				parent: "root",
				resolve: {
					project: function(){
						return project;
					},
					dataset: ["project", function(project){
						return project.dataset;
					}]
				}
			})
			.state("root.dataset.analysis", {					
				parent: "root.dataset"
			});
		}]);	
		beforeEach(module("test"));
		
		var $state, $rootScope;
		beforeEach(inject(function(_$state_, _$rootScope_){
			$state = _$state_;
			$rootScope = _$rootScope_;
		}));

		var mevContext; 
		beforeEach(inject(function(_mevContext_){
			mevContext = _mevContext_;
		}));
		it("ensures we can grab the context", function(){
			console.log("$state", $state);
			$rootScope.$apply(function(){
				$state.go("root.dataset.analysis");				
			});
			console.log("$state.current", $state.current);
			expect($state.current.name).toBe("root.dataset.analysis");
			expect(mevContext.root()).toBe(project);
			expect(true).toBe(true);	
			
		});

		var mevSelectionLocator;
		beforeEach(inject(function(_mevSelectionLocator_){
			mevSelectionLocator = _mevSelectionLocator_;
		}));
		it("ensures that can find selections", function(){
			$rootScope.$apply(function(){
				$state.go("root.dataset.analysis");				
			});
			console.log("$state.current", $state.current);
			expect($state.current.name).toBe("root.dataset.analysis");
			expect(mevContext.root()).toBe(project);
			expect(mevSelectionLocator.find("column")).toBe(project.dataset.selections.column);
			expect(mevSelectionLocator.find("column").length).toBe(2);
			expect(mevSelectionLocator.find("column")[0].name).toBe("s2");
			expect(true).toBe(true);	
		});
	});
});