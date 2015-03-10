define(["ng"], function(ng){
	var RowSetViewVM=function DatasetViewVM($scope, $stateParams, $state, dataset, rowSet){
		that=this;
		this.rowSet=rowSet;
		console.debug("RowSetViewVM", dataset, rowSet);
				
		this.baseUrl = '/annotations/'
            + $stateParams.datasetId
            + '/annotation';
		this.annotationsUrl = this.baseUrl + '/row/';
		this.annotationsUrl += rowSet.name+'/dataset/';
		
		var randomProjectId=Math.floor(Math.random()*11);
		if(rowSet.properties && rowSet.properties.selectionFacetLink){			
			this.annotationsUrl += rowSet.properties.selectionFacetLink;
		}else if (rowSet.properties){
			var facetUrl = "{\"facets\":[{\"c\":{\"type\":\"text\",\"name\":\"ID\",\"columnName\":\"MEVID\",\"mode\":\"regex\",\"caseSensitive\":false,\"query\":\""+
			rowSet.keys.join("|")
    		+"\"}}]}";     		
    		this.annotationsUrl += "project?project=MEV-"+randomProjectId+"&ui="+window.escape(facetUrl);			
		}else if(rowSet.name==="new"){
			this.annotationsUrl += "?"+randomProjectId;
		}
		
	};
	RowSetViewVM.$inject=["$scope", "$stateParams", "$state", "dataset", "rowSet"];
	return RowSetViewVM;
});
