define([], function(){
	
	var GeodsSummary = function (dataPromise, MevGeodsImportSrvc){
		//private members
		var self = this;
		
		//resolve promise
		var _data=dataPromise.then(function(data){
			self.gds=data.gds;
			self.gpl=data.gpl;
			self.gpl=data.gpl;
			self.title=data.title;
			self.summary=data.summary;
			self.platformtitle=data.platformtitle;
			self.n_samples=data.n_samples;
			self.samples=_mapSamples(data);
			self.datasetUrlRoot=data.ftplink;
		});
		
		//public members
		this.gds=null
		this.gpl=null;
		this.samples=null;
		this.datasetUrlRoot=null;		
		
		//private methods
		function _result(){
			return _data.$resolved ? _data : undefined;
		};
		function _mapSamples(data){
			var samples = {};
			for(var i=0;i<data.samples.length; i++){
				samples[data.samples[i].accession]=data.samples[i].title;
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
		this.getImportedDatasetPath=function(){
			var path = "/dataset/GDS"+self.gds+".soft/"; 
			return path;
		};
		this.put=function(success){
			MevGeodsImportSrvc.put({id: "test"}, _getPutParams(), success);
		}
		
		
	};
	
	return GeodsSummary;
});