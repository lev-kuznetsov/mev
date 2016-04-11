define(['./AnalysisClass'], function(AnalysisClass){

	//generateAnalysis :: !analysis.post, !analyses || Object, Object [, Function, Function] -> null
	return function(params, data, success, error){

		var self = this;
		
		self.analyses.push(AnalysisClass(self.analysis.post(params, data, success, error) ) );
		
		return null
	}
	
})