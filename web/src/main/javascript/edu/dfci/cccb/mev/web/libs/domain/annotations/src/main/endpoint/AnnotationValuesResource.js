define(["mui"], function(){
	function component($q, $resource, $stateParams, AnnotationProjectIdResource){
		var _self=this;
		var url="/annotations/:datasetName/annotation/:dimension/new/dataset/command/core/get-rows";
		this.AnnotationValuesResource = $resource(url, {
			datasetName: $stateParams.datasetId,
			limit: 30000
		});		
		
		//to get rows we first must chain a call to get-project-id
		this.get=function(dimension){
			return AnnotationProjectIdResource.get(dimension).then(function(data){
				if(data.project<=0)
					return $q.when({error: "OpenRefine - project not found"});
				else	
					data.datasetName=$stateParams.datasetId;
					data.dimension=dimension;
					return _self.AnnotationValuesResource.get(data).$promise;
				
				});
		};		
	}
	component.$name="AnnotationValuesResource";
	component.$inject=["$q", "$resource", "$stateParams", "AnnotationProjectIdResource"];
	component.$provider="service";
	return component;

});