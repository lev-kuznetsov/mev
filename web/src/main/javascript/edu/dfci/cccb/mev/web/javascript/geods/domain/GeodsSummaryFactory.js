define(["geods/domain/GeodsSummary",
        "geods/domain/GeodsSearchResult"], 
function(GeodsSummary, GeodsSearchResult){
	var GeodsSummaryFactory = function(MevGeodsSummaryResourceSrvc, MevGeodsSearchResourceSrvc, MevGeodsImportSrvc, $q){
		//public
		var factory=this;
		this.create=function(raw){
			return new GeodsSummary($q.when(raw), MevGeodsImportSrvc);
		};
		this.get=function(id){
			return new GeodsSummary(
					MevGeodsSummaryResourceSrvc.get({id: id}).$promise
					.then(function(data){
						return data.result[_data.result.uids[0]];
					}), MevGeodsImportSrvc);			
		};
		this.search=function(keywords, page, max){
			var self=this;
			var itemsPerPage = max || 20;
			var pageNum = page || 0;
			self.accumulator={};
			self.accumulator.uids=null;
			self.accumulator.summaries=null;
			return new GeodsSearchResult(					
					MevGeodsSearchResourceSrvc.get({
						term: "gds[Entry Type] AND ("+keywords+")",
						retmax: itemsPerPage,
						retstart: itemsPerPage * pageNum
					}).$promise
					.then(function(data){
						var uids = [];
						for(var i=0;i<data.esearchresult.idlist.length;i++){
							uids.push(Number(data.esearchresult.idlist[i]));
						}
						self.accumulator.uids=data;
						return MevGeodsSummaryResourceSrvc.get({
							id: uids.join()
						}).$promise;
					
					})
					.then(function(data){
						self.accumulator.summaries=data;
						return self.accumulator;
					}),
					factory);						
		};
	};
	return GeodsSummaryFactory;
});