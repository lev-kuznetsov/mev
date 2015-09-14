define(["ng"], function(ng){
	var TopGoModalDirective = function(alertService, topGoVM) { 
         return {
             restrict : 'C',
             scope : {
             	dataset : '=heatmapDataset'
             },
             templateUrl : "app/widgets/analysis/topgo/modal/topgoModal.tpl.html",
             link : function(scope, elems, attrs) {
                 scope.topGoVM = topGoVM;
                 scope.params = {
                        name: undefined,
                 		geneSelection: undefined,
                 		species: undefined,
                 		goType: undefined,
                 		testType: undefined,
                 		pAdjust: undefined,
                 		nodeSize: undefined
                 };
             	var getPostData = function(){
             		var postRequest = {
             			name: scope.params.name,
             			datasetName : scope.dataset.datasetName,
             			genelist : scope.params.geneSelection.keys,
             			species : scope.params.species,
             			goType : scope.params.goType,
             			testType : scope.params.testType,
             			pAdjust : scope.params.pAdjust,
             			nodeSize : scope.params.nodeSize
             		};             		
             		return postRequest;                                		
             	};
                scope.testInit = function(){                	
                	console.debug("TOPGO params", scope.params);                    			
                	var postData = getPostData();
                	console.debug("TOPGO postData", postData);                    			
        			scope.dataset.analysis.post({
        				datasetName : scope.dataset.datasetName, 
        				analysisType : "topgo"
        			}, postData);
                };
             }
         };
	};
	TopGoModalDirective.$inject=["alertService", "TopGoVM"];
	return TopGoModalDirective;
});