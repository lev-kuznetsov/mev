define(["ng"], function(ng){
	var selectionSetViewVM=function DatasetViewVM($scope, $stateParams, $state, dataset, selectionSet){
		that=this;
		this.selectionSet=selectionSet;
		console.debug("selectionSetViewVM", dataset, selectionSet);
				
		this.baseUrl = '/annotations/'
            + $stateParams.datasetId
            + '/annotation';
		this.annotationsUrl = this.baseUrl + '/'+selectionSet.type+'/';
		this.annotationsUrl += selectionSet.name+'/dataset/';
		
		var randomProjectId=Math.floor(Math.random()*11);
		
		if(selectionSet.properties && selectionSet.properties.selectionFacetLink 
				&& selectionSet.properties.selectionFacetLink.indexOf(".starred")===-1
				&& selectionSet.properties.selectionFacetLink.indexOf(".flagged")===-1){			
			this.annotationsUrl += selectionSet.properties.selectionFacetLink;
		}else if (selectionSet.properties){
			var facetUrl = "{\"facets\":[{\"c\":{\"type\":\"text\",\"name\":\"ID\",\"columnName\":\"MEVID\",\"mode\":\"regex\",\"caseSensitive\":false,\"query\":\""+
			selectionSet.keys.map(function(key){
				return "^"+key+"$";
			}).join("|")
    		+"\"}}]}";     		
    		this.annotationsUrl += "project?project=MEV-"+randomProjectId+"&ui="+window.escape(facetUrl);			
		}else if(selectionSet.name==="new"){
			this.annotationsUrl += "?"+randomProjectId;
		}
		
	};
	selectionSetViewVM.$inject=["$scope", "$stateParams", "$state", "dataset", "selectionSet"];
	selectionSetViewVM.$provider="controller";
	selectionSetViewVM.$name="SelectionSetViewVM";
	return selectionSetViewVM;
});
