define(["angular", 
        "../ClinicalSummary.module",
        "../domain/ClinicalSummaryRepository",
        "angular-route", 
        "angular-route"], 
function(angular, 
			angularModule, 
			ClinicalSummaryRepository){
	angular.module(angularModule.name).service("MevClinicalSummaryRepositorySrvc", 
	["$q", "MevAnnotationColumnsResource", "MevAnnotationRowsResource",
	function($q, MevAnnotationColumnsResource, MevAnnotationRowsResource){
		this.create=function(){
			var columns = MevAnnotationColumnsResource.get();
			var rows = MevAnnotationRowsResource.get();
			var dataPromise=$q.all({
				columns:  columns,
				rows: rows
			});
//			var dataPromise = MevAnnotationColumnsResource.get().then(MevAnnotationRowsResource.get);
			return new ClinicalSummaryRepository(dataPromise);
		};
	}])
	//call to OpenRefine to get all row data	
	.service("MevAnnotationRowsResource", 
	["$q", "$resource", "$routeParams", "$stateParams", "MevAnnotationProjectIdResource", 
    function($q, $resource, $routeParams, $stateParams, MevAnnotationProjectIdResource){
		var _self=this;
		var url="/annotations/:datasetName/annotation/column/new/dataset/command/core/get-rows";
		this.AnnotationRowsResource = $resource(url, {
			datasetName: $routeParams.datasetName || $stateParams.datasetId
		});		
		
		//to get rows we first must chain a call to get-project-id
		this.get=function(){
			return MevAnnotationProjectIdResource.get({}).then(function(data){
				if(data.project<=0)
					return $q.when({error: "OpenRefine - project not found"});
				else	
					data.datasetName=$routeParams.datasetName || $stateParams.datasetId;
					return _self.AnnotationRowsResource.get(data).$promise;
				
				});
		};		
	}])
	//call to OpenRefine to get header column info
	.service("MevAnnotationColumnsResource", ["$q", "$resource", "$routeParams", "$stateParams", "MevAnnotationProjectIdResource", 
	 function($q, $resource, $routeParams, $stateParams, MevAnnotationProjectIdResource){
		var _self=this;
		var url="/annotations/:datasetName/annotation/column/new/dataset/command/core/get-columns-info";		
		this.AnnotationColumnsResource = $resource(
				url, 
				{datasetName: $routeParams.datasetName || $stateParams.datasetId},
				{get: {method: "GET", isArray: true}
		});		
		
		//to get rows we first must chain a call to get-project-id
		this.get=function(){
			return MevAnnotationProjectIdResource.get().then(function(data){
				if(data.project<=0)
					return $q.when({error: -1})
				else
					data.datasetName=$routeParams.datasetName || $stateParams.datasetId;
					return _self.AnnotationColumnsResource.get(data).$promise;
				});
		};		
	}])
	//call to OpenRefine to find out current project id
	.service("MevAnnotationProjectIdResource", ["$resource", "$routeParams", "$stateParams", 
	 function($resource, $routeParams, $stateParams){
		
		url="/annotations/:datasetName/annotation/column/get-project-id";
		var AnnotationProjectIdResource = $resource(
					url, 
					{format: "json"}
				);		
		
		this.get=function(){
			return AnnotationProjectIdResource.get({datasetName: $routeParams.datasetName || $stateParams.datasetId}).$promise;			
		};
		
	}]);
});