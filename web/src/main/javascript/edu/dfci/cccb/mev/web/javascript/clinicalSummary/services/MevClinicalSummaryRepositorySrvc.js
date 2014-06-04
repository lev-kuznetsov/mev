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
	//call to OpenRefine to get all row data	
	.service("MevAnnotationRowsResource", ["$resource", "$routeParams", "MevAnnotationProjectIdResource", 
     function($resource, $routeParams, MevAnnotationProjectIdResource){
		console.debug("init MevAnnotationRowsResource");
		var _self=this;
		var url="/annotations/:datasetName/annotation/column/new/dataset/command/core/get-rows";
		this.AnnotationRowsResource = $resource(url, {
			datasetName: $routeParams.datasetName
		});		
		
		//to get rows we first must chain a call to get-project-id
		this.get=function(){
			return MevAnnotationProjectIdResource.get({}).then(function(data){return _self.AnnotationRowsResource.get(data).$promise;});
		};		
	}])
	//call to OpenRefine to get header column info
	.service("MevAnnotationColumnsResource", ["$resource", "$routeParams", "MevAnnotationProjectIdResource", 
	 function($resource, $routeParams, MevAnnotationProjectIdResource){
		console.debug("init MevAnnotationColumnsResource");
		var _self=this;
		var url="/annotations/:datasetName/annotation/column/new/dataset/command/core/get-columns-info";		
		this.AnnotationColumnsResource = $resource(
				url, 
				{datasetName: $routeParams.datasetName},
				{get: {method: "GET", isArray: true}
		});		
		
		//to get rows we first must chain a call to get-project-id
		this.get=function(){
			console.debug("MevAnnotationColumnsResource.get()");
			return MevAnnotationProjectIdResource.get().then(function(data){return _self.AnnotationColumnsResource.get(data).$promise;});
		};		
	}])
	//call to OpenRefine to find out current project id
	.service("MevAnnotationProjectIdResource", ["$resource", "$routeParams", 
	 function($resource, $routeParams){
		console.debug("int MevAnnotationProjectIdResource");
		
		url="/annotations/:datasetName/annotation/column/get-project-id";
		var AnnotationProjectIdResource = $resource(
					url, 
					{format: "json"}
				);		
		
		this.get=function(){
			console.debug("==> GET PROJECT ID", $routeParams);
			return AnnotationProjectIdResource.get({datasetName: $routeParams.datasetName}).$promise;			
		};
		
	}]);
});