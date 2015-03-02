define(["ng", "lodash"], function(ng, _){
	var module = ng.module("mui.views.project.dataset", []);
	module.config(['$stateProvider', function($stateProvider){
		var counter=0;
		$stateProvider.state("root.project.dataset", {
			parent: "root.project",
			url: "dataset/{datasetId:int}/",
			template: "<div>Project Dataset View: {{ProjectDatasetVM.datasetId}} dataset: {{ProjectDatasetVM.dataset.name}} counter: {{ProjectDatasetVM.counter}}<ui-view></ui-view></div>",
//			templateUrl: "app/views/project/dataset/templates/project.dataset.tpl.html",
			controller: ["$scope", "$stateParams", "counter", "dataset", function($scope, $stateParams, counter, dataset){
				this.datasetId=$stateParams.datasetId;
				this.counter=counter;
				this.dataset=dataset;
			}],
			controllerAs: "ProjectDatasetVM",
			resolve: {
				counter: function(){return counter++;},
				dataset: ["$stateParams", "project", function($stateParams, project){
					console.debug("root.project.dataset resolve dataset", project, $stateParams);
					
					var dataset = _.find(project.dataset, function(dataset){ return dataset.id===$stateParams.datasetId; });
					console.debug("root.project.dataset", dataset);
					return dataset;
				}]
			}
		})
		.state("root.project.dataset.samples", {
			parent: "root.project.dataset",
			url: "samples/",
			template: "<div>Samples: {{DatasetSamplesVM.datasetId}} {{DatasetSamplesVM.samples.keys}} <ui-view></ui-view></div>",
//			templateUrl: "app/views/project/dataset/templates/project.dataset.tpl.html",
			controller: ["$scope", "$stateParams", "counter", "samples", function($scope, $stateParams, counter, samples){
				this.datasetId=$stateParams.datasetId;
				this.samples=samples;
				console.debug("DatasetSamplesVM.controller", counter, samples);
			}],			
			controllerAs: "DatasetSamplesVM",
			resolve: {
				counter: function(){return counter++;},
				samples: ["$stateParams", "dataset", function($stateParams, dataset){
					return dataset.dataref.column;
				}]		
			}
		})
		.state("root.project.dataset.rows", {
			parent: "root.project.dataset",
			url: "rows/",
			template: "<div>Rows: {{DatasetRowsVM.row.keys}} counter: {{DatasetRowsVM.counter}}<ui-view></ui-view></div>",
//			templateUrl: "app/views/project/dataset/templates/project.dataset.tpl.html",
			controller: ["$scope", "$stateParams", "counter", "row", function($scope, $stateParams, counter, row){
				this.datasetId=$stateParams.datasetId;
				this.counter=counter;
				this.row=row;
				console.debug("DatasetRowsVM.controller", counter, row);
			}],			
			controllerAs: "DatasetRowsVM",
			resolve: {
				counter: function(){return counter++;},
				row: ["$stateParams", "dataset", function($stateParams, dataset){
					return dataset.dataref.row;
				}]
			}
		});
	}]);
	return module;
});