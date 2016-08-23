define(["ng", "lodash", "./ImportVM"],
		function(ng, _, ImportVM, mevWorkspace){
			var module = ng.module("mui.views.import", arguments, arguments);
			module.config(['$stateProvider', '$urlRouterProvider',
			     	function($stateProvider, $urlRouterProvider){	     				
			     		$stateProvider.state("root.import", {
			     			url: "/datasource/:datasourceId/:datasetId",
			     			templateUrl: "app/views/import/import.tpl.html",
			     			controller: "ImportVM",
			     			controllerAs: "ImportVM",
			     			parent: "root",
			     			resolve: {
			     				datasource: ["DatasourceRepository", "$stateParams", function(DatasourceRepository, $stateParams){
			     					return DatasourceRepository.get($stateParams.datasourceId);
			     				}], 
			     				dataset: ["datasource", "$stateParams", function(datasource, $stateParams){
			     					var findDataset=function(response){
			     						var dataset= _.filter(response.dataset, function(item){
				     						return item.id==$stateParams.datasetId;				     						
				     					});
			     						console.debug("resolved import dataset", dataset);
			     						return dataset[0];
			     					};
			     					if(datasource.$promise)
			     						return datasource.$promise.then(findDataset);
			     					else
			     						return findDataset(datasource)
			     				}]
			     			}
			     		})
//			     		.state("home.datasource.tcga", {
////			     			url: "/home/datasource/tcga",
//			     			templateUrl: "app/views/home/_datasource/templates/datasource.import.tpl.html",
////			     			controller: "HomeVM",
////			     			controllerAs: "HomeVM"
//			     		})
			}]);
			module.controller("ImportVM", ImportVM);
			return module;
		});