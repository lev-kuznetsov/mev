define(["lodash"], function(_){
	var SurvivalColumnList = function(mevAnnotationsLocator){
		this.get=function(){
			var results={
				columns: []
			};
			mevAnnotationsLocator.find("column").getFields().then(function(data){
				// results.projectId=data.project;
				// MevAnnotationColumnsResource.get().then(function(columns){
//					_.filter(columns, function(column, index, array){
//						if(column.is_numeric===true)
//							result.push(column);
//					});
					if(Array.isArray(columns)){					
						columns.map(function(column){
							results.columns.push(column);
						});
					}
				// });
			});
			
			return results;
		};
	};
	SurvivalColumnList.$name="mevSurvivalColumnList"
	SurvivalColumnList.$inject=["mevAnnotationsLocator"];
	SurvivalColumnList.$provider="service";
	return SurvivalColumnList;
});