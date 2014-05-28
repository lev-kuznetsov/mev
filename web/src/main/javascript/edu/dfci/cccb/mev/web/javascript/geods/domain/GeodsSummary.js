define([], function(){
	
	var GeodsSummary = function (resourceSvc, id){
		//private members
		var self = this;		
		var _resourceSvc=resourceSvc;		
		var _data=resourceSvc.get({id: id}, function(data){
			_data=data;
			_mapSamples(data);		
		});
		var _samples = [];

		//public members
		this.gpl=null;
		this.samples=null;
		this.datasetUrl=null;
		this.platformUrl=null;
		
		//private methods
		function _result(){
			return _data.$resolved ? _data.result[_data.result.uids[0]] : undefined;
		};
		function _mapSamples(data){
			for(var i=0;i<_result().samples.length; i++){
				_samples.push({
					key: _result().samples[i].accession,
					title: _result().samples[i].title				
				});
			}
			return _samples;
		}
		
		//privileged		
		this.id=id;
		this.load=function(id){
			_data = _resourceSvc.get({id: id});
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
		
		
		
	};
	
	return GeodsSummary;
});