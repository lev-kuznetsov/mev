define(["mui",
        "pouchdb",
        "./_controllers/DatasetProjectViewVM",
        "./_controllers/DatasetHomeVM",
        "./_controllers/DatasetHeatmapVMFactory",
        "./annotations/AnnotationsViewVM",
        "./columnSets/views.dataset.columnSets.module",
        "./rowSets/views.dataset.rowSets.module",
        "./selectionSets/views.dataset.selectionSets.module",
        "./analysis/views.dataset.analysis.module",
        "./analyses/views.dataset.analyses.module",
        "./session/views.dataset.session.module",
		"js/project/Project",
		"js/alertservice/AlertService",
		"js/setmanager/SetManager",
		'js-data-angular',
		"../../domain/domain.module",
		"../../widgets/analysis/widgets.analysis.module",
		'ag-grid',
		'crossfilter',
		'blueimp-canvas-to-blob',
		"notific8",
        "mev-analysis",
        "mev-bs-modal",
        "mevPathwayEnrichment",
        "mev-gsea",
        "mev-annotations",
        "mev-hcl",
        "mev-heatmap",
        "mev-domain-common",
		"mev-topgo",
		"mev-normalization",
		"mev-edger",
		"mev-wgcna",
		"mev-limma",
		"mev-ttest",
		"mev-anova",
		"mev-deseq",
		"mev-voom",
		"mev-kmeans",
		"mev-survival"],
function(ng,
		PouchDB,
		DatasetProjectViewVM,
		DatasetHomeVM,
		DatasetHeatmapVMFactory,
		AnnotationsViewVM
		){	"use strict";
	var module=ng.module("mui.views.dataset", arguments, arguments);
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
							headerUrl: "app/views/dataset/_templates/dataset.header.tpl.html"
			   	     	},
	   	     			resolve:{
	   	     				datasetResource: ["$state", "$stateParams", "mevDatasetRest", "$q", "$http", function($state, $stateParams, DatasetResourceService, $q, $http){
	   	     					
		   	     				var datasetResource = DatasetResourceService.get({
		   	     					datasetName: $stateParams.datasetId
		   	     				}, function(response){
		   	     				    console.debug("**** Loaded Dataset", $stateParams.datasetId, response);
	//	   	     				    LoadingModal.hide();
		   	     				}, function(error){
	//	   	     					downloadFailure();
		   	     					console.debug("**** Failed to Load Dataset", $stateParams.datasetId, error);
									if(error.data && _.includes(error.data, "DatasetNotFoundException"))
										$state.go("root.datasets.sessionTimeout");
									else
										$state.go("root.datasets.error", {
											header: "Parsing Error",
											message: "Unable to parse data file. Two common causes are duplicate row keys or incorrect column header.",
											error: error})
		   	     				});	   	     					
		   	     				return datasetResource.$promise;		   	     				
	   	     				}],
	   	     				project: ["$state", "$stateParams", "datasetResource", "ProjectFactory",
	   	     				function($state, $stateParams, datasetResource, ProjectFactory){
	   	     						return datasetResource.$promise.then(function(response){
			   	     					var project = ProjectFactory($stateParams.datasetId, datasetResource);	   	     					
			   	     					console.debug("***Project", project);	   	     					
			   	     					return project;
			   	     				});	   	     					
	   	     				}],	
	   	     				dataset: ["$state", "$stateParams", "project", "mevAnnotationRepository",
	   	     				function($state, $stateParams, project){
	   	     					console.info("***resolving dataset", $stateParams.datasetId, $stateParams, $state, project);
	   	     					return project.dataset.loadAnalyses()
									.then(function(){
										console.info("***resolved dataset", project.dataset);
										return project.dataset;
									})
									.then(function(dataset){
										var mockORefineProject = {
											metadata: {
												customMetadata: {
													datasetName: dataset.id
												}
											}
										};
										function handleNotFound(e){
											if(e.name === "AnnotationNotFoundOnServer")
												;//this is ok, annotations may not have been uploaded yet
											else
												throw e;
										}
										dataset.getAnnotations("column").saveAnnotations(mockORefineProject)
											.catch(handleNotFound);
										dataset.getAnnotations("row").saveAnnotations(mockORefineProject)
											.catch(handleNotFound);
										return dataset;
									});
	   	     				}]	   	     				
	   	     			},
						onExit: ["dataset", function(dataset){
							console.log("closing " + dataset.id);
							dataset.close();
						}]
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