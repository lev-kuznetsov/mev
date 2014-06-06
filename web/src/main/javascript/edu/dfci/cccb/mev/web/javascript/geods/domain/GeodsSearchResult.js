define([], function(){
	var self=this;
	var GeodsSearchResult=function(dataPromise, GeodsSummaryFactory){		
		//private
		var _self=this;
		_self.GeodsSummaryFactory=GeodsSummaryFactory;
		_self.summaries=null;
		function _parseSummaries(data){
			var summaries=[];
			for(var uid in data.summaries.result){
				if(uid!="uids"){
					summaries.push(
						new _self.GeodsSummaryFactory.create(data.summaries.result[uid])
					);
				}
			}
			return summaries;
		}
		
		//resolve promise
		var _initPromise = dataPromise.then(function(data){			
			_self.results=data;
			_self.summaries=_parseSummaries(data);
		});
		
	};
	return GeodsSearchResult;
});