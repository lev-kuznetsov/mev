define(["ng", 
        "./_controllers/DatasetViewVM", 
        "./_controllers/DatasetProjectViewVM",
        "./_controllers/DatasetHomeVM",
        "./_controllers/DatasetHeatmapVMFactory",
        "./annotations/AnnotationsViewVM",
        "./columnSets/views.dataset.columnSets.module",
        "./rowSets/views.dataset.rowSets.module",
        "./selectionSets/views.dataset.selectionSets.module",
        "./analysis/views.dataset.analysis.module",
        "./analyses/views.dataset.analyses.module"], 
function(ng, 
		DatasetViewVM, 
		DatasetProjectViewVM,
		DatasetHomeVM,
		DatasetHeatmapVMFactory,
		AnnotationsViewVM){
	var module=ng.module("mui.views.dataset", ["mui.views.dataset.columnSets", 
	                                           "mui.views.dataset.rowSets",
	                                           "mui.views.dataset.SelectionSets",
	                                           "mui.views.dataset.analysis",
	                                           "mui.views.dataset.analyses"]);
	
	module.controller("DatasetViewVM", DatasetViewVM);	
	module.controller("DatasetProjectViewVM", DatasetProjectViewVM);
	module.controller("DatasetHomeVM", DatasetHomeVM);	
	module.factory("DatasetHeatmapVMFactory", DatasetHeatmapVMFactory);	
	module.controller("AnnotationsViewVM", AnnotationsViewVM);
	module.config(['$stateProvider', '$urlRouterProvider',
	   	     	function($stateProvider, $urlRouterProvider){					
	   	     		$stateProvider	  
	   	     		.state("root.abstractDataset", {
	   	     			parent: "root",
	   	     			"abstract": true,
	   	     			url: "/dataset",
	   	     			breadcrumbProxy: "root.datasets",
   	     				displayName: "datasets",
   	     				template: "<ui-view></ui-view>"   	     					
	   	     		})
	   	     		.state("root.dataset", {	   	     			
	   	     			parent: "root.abstractDataset",
	   	     			"abstract": true,
	   	     			url: "/:datasetId/",	   	     			
//			   	     	params: {
//			   	     		datasetId: null
//			   	     	},	
	   	     			templateUrl: "app/views/dataset/_templates/dataset.tpl.html",
	   	     			breadcrumbProxy: "root.dataset.home",
	   	     			displayName: "{{dataset.datasetName}}",
//	   	     			template: "<div>dataset: {{$stateParams.datasetId}}</div>",	   	     			
	   	     			controller: "DatasetProjectViewVM",
	   	     			controllerAs: "DatasetProjectViewVM",
		   	     		data: {
			   	     		sidemenuUrl: "app/views/dataset/_templates/dataset.sidemenu.accordion.tpl.html",
			   	     		footerUrl: "app/views/dataset/_templates/dataset.footer.tpl.html",
			   	     	},
	   	     			resolve:{
		   	     			project: ["$stateParams", "ProjectFactory", "DatasetResourceService", function($stateParams, ProjectFactory, DatasetResourceService){
	   	     					
		   	     				var dataset = DatasetResourceService.get({
		   	     					datasetName: $stateParams.datasetId
		   	     				}, function(response){
		   	     				    console.debug("**** Loaded Dataset", $stateParams.datasetId, response);
	//	   	     				    LoadingModal.hide();
		   	     				}, function(error){
	//	   	     					downloadFailure();
		   	     					console.debug("**** Failed to Load Dataset", $stateParams.datasetId, error);
		   	     				});
	   	     					
		   	     				return dataset.$promise.then(function(response){
			   	     				var project = ProjectFactory($stateParams.datasetId, response);	   	     					
		   	     					console.debug("***Project", project);	   	     					
		   	     					return project;
		   	     				});
		   	     				
	   	     				}],
	   	     				dataset: ["$state", "$stateParams", "DatasetResourceService", "project",
	   	     				function($state, $stateParams, DatasetResourceService, project){
	   	     					console.info("***resolving dataset", $stateParams.datasetId, $stateParams, $state, project);
	   	     					return project.dataset
	   	     					.loadAnalyses().then(function(){
	   	     						console.info("***resolved dataset", project.dataset);
	   	     						return project.dataset;
	   	     					});	   	     					
	   	     				}]	   	     				
	   	     			}
	   	     		})
	   	     		.state("root.dataset.home", {		   	     		
	   	     			parent: "root.dataset",
	   	     			url: "",
	   	     			displayName: "{{dataset.datasetName}}",
	//   	     			"abstract": true,
	//		   	     	params: {
	//		   	     	   id: null
	//		   	     	},		   	     			
	   	     			views: {
	   	     				"": {	   	     					
	   	     					templateUrl: "app/views/dataset/_templates/dataset.home.tpl.html",
	   	     					//   	     			template: "<div>dataset: {{$stateParams.datasetId}}</div>",	   	     			
	   	     					controller: "DatasetHomeVM",
	   	     					controllerAs: "DatasetHomeVM",
	   	     				},
	   	     				"@root.dataset.analysis": {	   	     					
	   	     				},
		   	     			"child2@root.dataset.home": {
		   	     				template: "<h1>child2</h1>"
		   	     			}
	   	     			},
		   	     		data: {},
	   	     			resolve:{}
	   	     			
	   	     		})
	   	     		.state("dataset.annotations", {
	   	     			url: "annotations/:dimension",
//	   	     			template: "<div>{{dataset.name}} - {{annotations.name}}",
	   	     			template: "<div>{{AnnotationsViewVM.dataset.name}} - {{AnnotationsViewVM.annotations.name}}</div>",
	   	     			controller: "AnnotationsViewVM",
	   	     			controllerAs: "AnnotationsViewVM",
	   	     			parent: "dataset",
	   	     			resolve: {	   	     				
	   	     				annotations: ["$stateParams", "dataset", function($stateParams, dataset){	   	     					
	   	     					var annotations= dataset.annotations[$stateParams.dimension];
	   	     					console.debug("Dataset.Annotations view resolved: ", annotations);
	   	     					return annotations;
	   	     				}]
	   	     			}
	   	     		});
	   	}]);	
	return module;
});