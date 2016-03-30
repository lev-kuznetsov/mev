define(["lodash"], function(_){ "use strict";
	function PcaStateVMFactory(){ 
		function factory(scope, project, analysis){
			var _self = this;
			this.project = project;
			this.analysis = analysis;
			this.curSelection = [];
		  	this.getSelection=function(){
		  		return _self.curSelection;
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
		factory.$inject=["$scope", "project", "analysis"];		
		return factory;
	}	
	PcaStateVMFactory.$inject=[];
	PcaStateVMFactory.$name="PcaStateVMFactory";
	PcaStateVMFactory.$provider="factory";
	return PcaStateVMFactory;
});