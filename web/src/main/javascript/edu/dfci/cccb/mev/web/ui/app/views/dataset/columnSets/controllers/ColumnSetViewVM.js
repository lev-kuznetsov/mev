define(["ng"], function(ng){
	var ColumnSetViewVM=function DatasetViewVM($scope, $stateParams, $state, dataset, columnSet){
		that=this;
		this.columnSet=columnSet;
		console.debug("ColumnSetViewVM", dataset, columnSet);
				
		this.baseUrl = '/annotations/'
            + $stateParams.datasetId
            + '/annotation';
		this.annotationsUrl = this.baseUrl + '/column/';
		this.annotationsUrl += columnSet.name+'/dataset/';
		
		var randomProjectId=Math.floor(Math.random()*11);
		if(columnSet.properties && columnSet.properties.selectionFacetLink){			
			this.annotationsUrl += columnSet.properties.selectionFacetLink;
		}else if (columnSet.properties){
			var facetUrl = "{\"facets\":[{\"c\":{\"type\":\"text\",\"name\":\"ID\",\"columnName\":\"MEVID\",\"mode\":\"regex\",\"caseSensitive\":false,\"query\":\""+
			columnSet.keys.join("|")
    		+"\"}}]}";     		
    		this.annotationsUrl += "project?project=MEV-"+randomProjectId+"&ui="+window.escape(facetUrl);			
		}else if(columnSet.name==="new"){
			this.annotationsUrl += "?"+randomProjectId;
		}
		
	};
	ColumnSetViewVM.$inject=["$scope", "$stateParams", "$state", "dataset", "columnSet"];
	return ColumnSetViewVM;
});
