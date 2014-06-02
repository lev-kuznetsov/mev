define([], function(){
	
	var GeodsSummary = function (sourceSvc, destSvc, id){
		//private members
		var self = this;		
		var _sourceSvc=sourceSvc;		
		var _destSvc=destSvc;
		var _data=sourceSvc.get({id: id}, function(data){
			_data=data;
			self.gds=_result().gds;
			self.gpl=_result().gpl;
			self.gpl=_result().gpl;
			self.samples=_mapSamples(data);
			self.datasetUrlRoot=_result().ftplink;
		});
		
		//public members
		this.gds=null
		this.gpl=null;
		this.samples=null;
		this.datasetUrlRoot=null;		
		
		//private methods
		function _result(){
			return _data.$resolved ? _data.result[_data.result.uids[0]] : undefined;
		};
		function _mapSamples(data){
			var samples = {};
			for(var i=0;i<_result().samples.length; i++){
				samples[_result().samples[i].accession]=_result().samples[i].title;
			}
			return samples;
		}
		function _getPutParams(){
			return {
				"gds": self.gds,
				"gpl":self.gpl,
				"samples":self.samples,
				"datasetUrl": self.datasetUrlRoot
			};
		}
		//privileged		
		this.id=id;
		this.load=function(id){
			_data = _sourceSvc.get({id: id});
		};				
		this.getId=function(){
			return self.id;
		};
		this.getTitle=function(){
			return _result() === undefined ? undefined : _result().title;
		};
		this.getPlatformTitle=function(){
			return _result()=== undefined ? undefined : _result().platformTitle;
		};
		this.getSamples=function(){
			return _samples;
		};
		this.put=function(success){
			console.debug("_getPutParams", _getPutParams());
			_destSvc.put({id: "test"}, _getPutParams(), success);
		}
		
		
	};
	
	return GeodsSummary;
});