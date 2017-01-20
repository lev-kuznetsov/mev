define(["lodash"], function(_){
	function mevAnalysisLocator(mevContext){	
		function isAnalysisOfType(types, analysis){
			return _.find(types, function(type){
				return analysis.type === type; 
			});
		}
		this.find = function(type){
			var context = mevContext.current() || mevContext.root();
			if(!context) return;
			if(_.isArray(type)){
				if(context.type && isAnalysisOfType(type, context)){
					return [context];
				}else{
					return _.filter(mevContext.root().dataset.analyses, function(analysis){
						return isAnalysisOfType(type, analysis);
					});
				}
			}else if(_.isString(type)){
				if(context.type && context.type === type)
					return [mevContext.current()];
				else
					return _.filter(mevContext.root().dataset.analyses, function(analysis){
						return analysis.type === type;
					});
			}else if(_.isObject(type)){
				if(!type.name)
					throw new Error("meAnalysisLocator - must specify analysis name: " + JSON.stringify(type));
				return _.find(mevContext.root().dataset.analyses, function(analysis){
					return analysis.name === type.name;
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