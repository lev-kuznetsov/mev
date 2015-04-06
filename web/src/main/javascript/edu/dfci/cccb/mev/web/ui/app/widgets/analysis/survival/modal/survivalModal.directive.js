define(["ng"], function(ng){
	var SurvivalModalDirective = function(alertService, SurvivalColumnList, SurvivalAnalysisSrv) { 
         return {
             restrict : 'C',
             scope : {
             	dataset : '=heatmapDataset'
             },
             templateUrl : "app/widgets/analysis/survival/modal/survivalModal.tpl.html",
             link : function(scope, elems, attrs) {
                 
                 scope.params = {
                        name: undefined,
                 		column: undefined
                 };
             	var getPostData = function(){
             		var postRequest = {
             			name: scope.params.name,
             			datasetName : scope.dataset.datasetName,             			
             			experiment: scope.params.experiment,
             			experimentName: scope.params.experiment.name,
             			control: scope.params.control,
             			controlName: scope.params.control.name,
             		};             		
             		return postRequest;                                		
             	};  
             	
                scope.testInit = function(){                	
                	var postData = getPostData();
                	                		
            		SurvivalAnalysisSrv.getInputDataTcga(postData).then(function(data){                		
            			postData.input=data;
            			
            			console.debug("Survival postdata", postData);
            			                			
            			scope.dataset.analysis.post({
            				datasetName : scope.dataset.datasetName, 
            				analysisType : "survival"
            			}, postData);
            		})["catch"](function(e){
            			alertService.error("Could not launch Survival Analysis: " + e);
        				return postData;
            		});
                	
                };
                
                scope.columnList=SurvivalColumnList.get();
                scope.$on("openRefine:loadedAnnotations:column", function($event, theProject){
                	console.debug("openRefine:loadedAnnotations:column", theProject);
                	if(theProject.id!=scope.columnList.projectId){
                		console.debug("openRefine:loadedAnnotations:column reload");
                		scope.columnList=SurvivalColumnList.get();
                	}
                });
             }
         };
	};
	SurvivalModalDirective.$inject=["alertService", "SurvivalColumnList", "SurvivalAnalysisSrv"];
	return SurvivalModalDirective;
});