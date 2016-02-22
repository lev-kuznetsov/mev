define(["mui", "../model/AnnotationRepository"], 
function(ng, AnnotationRepository){
	
	function component($q, AnnotationColumnsResou, AnnotationRowsResource){
	
		return {
			$get: function(){
				var columns = AnnotationColumnsResource.get();
				var rows = AnnotationRowsResource.get();
				var dataPromise=$q.all({
					columns:  columns,
					rows: rows
				});
		//			var dataPromise = AnnotationColumnsResource.get().then(AnnotationRowsResource.get);
				return new AnnotationRepository(dataPromise);
			}
		};		
	}
	component.$inject=["$q", "AnnotationColumnsResource", "AnnotationRowsResource"];
	component.$name="ClinicalSummaryRepositorySrvc";
	component.$provider="service";
	return component;

});