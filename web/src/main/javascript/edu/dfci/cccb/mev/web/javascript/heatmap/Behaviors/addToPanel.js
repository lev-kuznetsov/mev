define([], function(){
	
	return function(analysis, dimension){
		
		var panelType = (dimension == "row")? "side" : "top";
		
		if (analysis.type == "Hierarchical Clustering"){
			this.panel[panelType] = analysis;
			this.labels[dimension] = analysis.keys;
        } else if (inputData.type == "K-means Clustering") {
            var keys = [];
            for (var i=0; i<analysis.clusters.length; i++){
                for (var j=0; j<analysis.clusters[i].length; j++){
                    keys.push(analysis.clusters[i][j])
                }
            }
            analysis.keys = keys;
            
            this.view.panel[panelType] = analysis;
            this.view.labels[dimension] = analysis.keys;
        }
	}
	
	
	
});