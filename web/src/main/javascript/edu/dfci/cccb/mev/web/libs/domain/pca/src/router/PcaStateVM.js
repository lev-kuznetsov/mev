define(["lodash"], function(_){ "use strict";
	function PcaStateVM(scope, project, analysis, mevAnalysisTypes){ 

		this.project = project;
		this.analysis = analysis;
		this.curSelection = [];
	  	scope.getSelection=function(){
	  		return scope.curSelection;
	  	};

	  	scope.sizeChanged=function(){
    		console.debug("pca resize");
    		window.dispatchEvent(new Event('resize'));
    	};
    	scope.$on("mui:dashboard:panel:rowMax", scope.sizeChanged);
		scope.$on("mui:dashboard:panel:rowMin", scope.sizeChanged);
		scope.$on("mui:dashboard:panel:max", scope.sizeChanged);
		scope.$on("mui:dashboard:panel:min", scope.sizeChanged);
	}	
	PcaStateVM.$inject=["$scope", "project", "analysis", "mevAnalysisTypes"];
	PcaStateVM.$name="PcaStateVM";
	PcaStateVM.$provider="controller";
	return PcaStateVM;
});