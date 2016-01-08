define(["ng"], function(ng){
	var PcaModalDirective = function(alertService, pcaVM) { 
         return {
             restrict : 'C',
             scope : {
             	dataset : '=heatmapDataset'
             },
             templateUrl : "app/widgets/analysis/pca/modal/pcaModal.tpl.html",
             link : function(scope, elems, attrs) {
                 scope.pcaVM = pcaVM;
                 scope.params = {
                        name: undefined
                 };
             	var getPostData = function(){
             		var postRequest = {
             			name: scope.params.name
             		};             		
             		return postRequest;                                		
             	};
                scope.testInit = function(){                	
                	console.debug("PCA params", scope.params);                    			
                	var postData = getPostData();
                	console.debug("PCA postData", postData);                    			
        			scope.dataset.analysis.post({
        				datasetName : scope.dataset.datasetName, 
        				analysisType : "pca"
        			}, postData);
                };
             }
         };
	};
	PcaModalDirective.$inject=["alertService", "PcaVM"];
	PcaModalDirective.$name="PcaModalDirective";
	return PcaModalDirective;
});