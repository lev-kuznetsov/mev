define(["mui"], function(){
	function component($resource, $stateParams){
		url="/annotations/:datasetName/annotation/:dimension/get-project-id";
		var AnnotationProjectIdResource = $resource(
			url, 
			{format: "json"}
		);		
		
		this.get=function(dimension){
			return AnnotationProjectIdResource.get({
				datasetName: $stateParams.datasetId,
				dimension: dimension || "column"
			}).$promise;			
		};
	}
	component.$name="AnnotationProjectIdResource";
	component.$inject=["$resource", "$stateParams"];
	component.$provider="service";
	return component;

});