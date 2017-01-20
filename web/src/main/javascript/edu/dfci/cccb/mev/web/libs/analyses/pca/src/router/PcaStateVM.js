define(["lodash"], function(_){ "use strict";
	function PcaStateVMFactory(mevAnalysisTypes, mevLimmaAnalysisType, mevEdgerAnalysisType, mevVoomAnalysisType,
							   mevtTestAnalysisType, mevDeseqAnalysisType, mevAnovaAnalysisType){
		function factory(scope, project, analysis){
			var _self = this;
			this.project = project;
			this.analysis = analysis;
			this.curSelection = [];
			this.curGroups = [];
			this.curSelections = [];
			this.analysisTypes = mevAnalysisTypes.all();
		  	this.getSelection=function(){
		  		return _self.curSelection;
		  	};
			this.analysis.getExperiment=function(){
				return _self.curGroups[0];
			};
			this.analysis.getControl=function(){
				return _self.curGroups[1];;
			};
			this.analysis.getSelections=function(){
				return _self.curSelections;
			}
		  	scope.sizeChanged=function(){
	    		console.debug("pca resize");
	    		window.dispatchEvent(new Event('resize'));
	    	};
	    	scope.$on("mui:dashboard:panel:rowMax", scope.sizeChanged);
			scope.$on("mui:dashboard:panel:rowMin", scope.sizeChanged);
			scope.$on("mui:dashboard:panel:max", scope.sizeChanged);
			scope.$on("mui:dashboard:panel:min", scope.sizeChanged);	
		}
		factory.$inject=["$scope", "project", "analysis"];		
		return factory;
	}	
	PcaStateVMFactory.$inject=["mevAnalysisTypes", "mevLimmaAnalysisType", "mevEdgerAnalysisType", "mevVoomAnalysisType",
		"mevtTestAnalysisType", "mevDeseqAnalysisType", "mevAnovaAnalysisType"];
	PcaStateVMFactory.$name="PcaStateVMFactory";
	PcaStateVMFactory.$provider="factory";
	return PcaStateVMFactory;
});