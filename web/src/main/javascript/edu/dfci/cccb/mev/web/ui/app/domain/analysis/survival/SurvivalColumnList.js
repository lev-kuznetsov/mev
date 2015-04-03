define(["ng", "lodash"], function(ng, _){
	var SurvivalColumnList = function(MevAnnotationProjectIdResource, MevAnnotationColumnsResource){
		this.get=function(){
			var results={
				columns: []
			};			
			MevAnnotationProjectIdResource.get().then(function(data){
				results.projectId=data.project;
				MevAnnotationColumnsResource.get().then(function(columns){
//					_.filter(columns, function(column, index, array){
//						if(column.is_numeric===true)
//							result.push(column);
//					});
					if(Array.isArray(columns)){					
						columns.map(function(column){
							results.columns.push(column);
						});
					}
				});
			});
			
			return results;
		};
	};
	SurvivalColumnList.$inject=["MevAnnotationProjectIdResource", "MevAnnotationColumnsResource"];
	return SurvivalColumnList;
});