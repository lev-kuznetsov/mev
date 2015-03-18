define(["ng", 
        "./controllers/DatasetViewVM", 
        "./controllers/DatasetProjectViewVM",
        "./controllers/DatasetHomeVM",
        "./annotations/AnnotationsViewVM",
        "./columnSets/views.dataset.columnSets.module",
        "./rowSets/views.dataset.rowSets.module",
        "./analysis/views.dataset.analysis.module"], 
function(ng, 
		DatasetViewVM, 
		DatasetProjectViewVM,
		DatasetHomeVM,
		AnnotationsViewVM){
	var module=ng.module("mui.views.dataset", ["mui.views.dataset.columnSets", 
	                                           "mui.views.dataset.rowSets",
	                                           "mui.views.dataset.analysis"]);
	
	module.controller("DatasetViewVM", DatasetViewVM);	
	module.controller("DatasetProjectViewVM", DatasetProjectViewVM);
	module.controller("DatasetHomeVM", DatasetHomeVM);	
	module.controller("AnnotationsViewVM", AnnotationsViewVM);
	module.config(['$stateProvider', '$urlRouterProvider',
	   	     	function($stateProvider, $urlRouterProvider){	     				
	   	     		$stateProvider	   	     		
	   	     		.state("root.dataset", {	   	     			
	   	     			parent: "root",
	   	     			"abstract": true,
	   	     			url: "/dataset/:datasetId/",
//			   	     	params: {
//			   	     		datasetId: null
//			   	     	},	
	   	     			templateUrl: "app/views/dataset/templates/dataset.tpl.html",
//	   	     			template: "<div>dataset: {{$stateParams.datasetId}}</div>",	   	     			
	   	     			controller: "DatasetProjectViewVM",
	   	     			controllerAs: "DatasetProjectViewVM",
		   	     		data: {
			   	     		sidemenuUrl: "app/views/dataset/templates/dataset.sidemenu.accordion.tpl.html",
			   	     		footerUrl: "app/views/dataset/templates/dataset.footer.tpl.html",
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
	   	     			url: "home/",
	//   	     			"abstract": true,
	//		   	     	params: {
	//		   	     	   id: null
	//		   	     	},	
	   	     			templateUrl: "app/views/dataset/templates/dataset.home.tpl.html",
	//   	     			template: "<div>dataset: {{$stateParams.datasetId}}</div>",	   	     			
	   	     			controller: "DatasetHomeVM",
	   	     			controllerAs: "DatasetHomeVM",
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