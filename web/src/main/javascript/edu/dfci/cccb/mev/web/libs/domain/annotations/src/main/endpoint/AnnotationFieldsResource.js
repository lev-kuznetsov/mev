define(["mui"], function(){
	function component($q, $resource, $routeParams, $stateParams, AnnotationProjectIdResource){
		var _self=this;
		var url="/annotations/:datasetName/annotation/:dimension/new/dataset/command/core/get-columns-info";		
		this.AnnotationFieldsResource = $resource(
				url, 
				{datasetName: $routeParams.datasetName || $stateParams.datasetId},
				{get: {method: "GET", isArray: true}
		});		
		
		//to get rows we first must chain a call to get-project-id
		this.get=function(dimension){
			return AnnotationProjectIdResource.get(dimension).then(function(data){
				if(data.project<=0)
					return $q.when({error: -1});
				else
					data.datasetName=$routeParams.datasetName || $stateParams.datasetId;
					data.dimension=dimension;
					var fieldsPromise = _self.AnnotationFieldsResource.get(data).$promise;
					fieldsPromise.params = data;
					return fieldsPromise;
				});
		};			
	}
	component.$name="AnnotationFieldsResource";
	component.$inject=["$q", "$resource", "$routeParams", "$stateParams", "AnnotationProjectIdResource"];
	component.$provider="service";
	return component;

});