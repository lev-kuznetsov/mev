define(["lodash"], function(_){
	function mevAnalysisLocator(mevContext){		
		this.find = function(type){
			if(type){
				if(mevContext.current().type && mevContext.current().type === type)
					return [mevContext.current()];
				else
					return _.filter(mevContext.root().dataset.analyses, function(analysis){
						return analysis.type === type;
					});
			}else{
				return mevContext.root().dataset.analysis;
			}
		};
	}
	mevAnalysisLocator.$inject=["mevContext"];
	mevAnalysisLocator.$name="mevAnalysisLocator";
	mevAnalysisLocator.$provider="service";
	return mevAnalysisLocator;
});