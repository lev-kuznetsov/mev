define(["angular", 
        "clinical/ClinicalSummary.module",
        "clinical/domain/ClinicalSummaryReporitory",
        "angularResource", 
        "angularRoute"], 
function(angular, 
			angularModule, 
			ClinicalSummaryReporitory){
	angular.module(angularModule.name).service("MevClinicalSummaryRepositorySrvc", ["$q", "MevAnnotationColumnsResource", "MevAnnotationRowsResource",
	function($q, MevAnnotationColumnsResource, MevAnnotationRowsResource){
		console.debug("init MevClinicalSummaryRepositorySrvc");
		this.create=function(){
			console.debug("MevClinicalSummaryRepositorySrvc.create()");
			var columns = MevAnnotationColumnsResource.get();
			console.debug("MevClinicalSummaryRepositorySrvc.columns", columns);
			var rows = MevAnnotationRowsResource.get();
			console.debug("MevClinicalSummaryRepositorySrvc.rows", rows);
			var dataPromise=$q.all({
				columns:  columns,
				rows: rows
			});
//			var dataPromise = MevAnnotationColumnsResource.get().then(MevAnnotationRowsResource.get);
			return new ClinicalSummaryReporitory(dataPromise);
		};
	}])
	.service("MevAnnotationRowsResource", ["$resource", "$routeParams", "MevAnnotationProjectIdResource", 
     function($resource, $routeParams, MevAnnotationProjectIdResource){
		console.debug("init MevAnnotationRowsResource");
		var _self=this;
		var url="/annotations/aaa/annotation/column/new/dataset/command/core/get-rows";
		this.AnnotationRowsResource = $resource(url, {
			datasetName: $routeParams.datasetName
		});		
		this.get=function(){
			return MevAnnotationProjectIdResource.get().then(function(data){return _self.AnnotationRowsResource.get(data).$promise;});
		};		
	}])
	.service("MevAnnotationColumnsResource", ["$resource", "$routeParams", "MevAnnotationProjectIdResource", 
	 function($resource, $routeParams, MevAnnotationProjectIdResource){
		console.debug("init MevAnnotationColumnsResource");
		var _self=this;
		var url="/annotations/:datasetName/annotation/column/new/dataset/command/core/get-columns-info";		
		this.AnnotationColumnsResource = $resource(url, {
			datasetName: $routeParams.datasetName
		},
		{get: {method: "GET", isArray: true}});		
		this.get=function(){
			console.debug("MevAnnotationColumnsResource.get()");
			return MevAnnotationProjectIdResource.get().then(function(data){return _self.AnnotationColumnsResource.get(data).$promise;});
		};		
	}])
	.service("MevAnnotationProjectIdResource", ["$resource", "$routeParams", 
	 function($resource, $routeParams){
		console.debug("int MevAnnotationProjectIdResource");
		url="/annotations/:datasetName/annotation/column/get-project-id";
		var AnnotationProjectIdResource = $resource(url, {
			datasetName: $routeParams.datasetName,
			format: "json"
		});		
		this.get=function(){
			return AnnotationProjectIdResource.get().$promise;			
		};
		
	}]);
});