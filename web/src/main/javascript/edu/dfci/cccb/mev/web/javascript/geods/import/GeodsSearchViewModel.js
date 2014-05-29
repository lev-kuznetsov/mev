define([],function(){
	var GeodsSearchViewModel = function(GeodsSummaryFactory, scope, $modal, $location){
		var self=this;
		this._scope=scope;
		var _GeodsSummaryFactory=GeodsSummaryFactory;
		
		this.searchById=function(){
			console.debug("vm.searchById", self.uid);
			self.summary=_GeodsSummaryFactory.create(self.uid);		
		};
		
		var _showPleaseWait=function(){
			console.debug("mv.test()");
			var modalInstance = $modal.open(
					{
						template: '<div style="padding: 5em"><p> Please wait while, downloading GEO dataset ... </p><div>',
						size: 'lg'
					});
			return modalInstance;
		};
		
		var _getImportedDatasetPath=function(){
			var path = "/dataset/GDS"+self.summary.gds+".soft"; 
			console.debug("_getImportedDatasetPath()", path);
			return path;
		};
		
		this.import=function(){
			var modal = _showPleaseWait();
			console.debug("GeodsSearchViewModel.import()");
			self.summary.put(function(data){				
				console.debug("data", data);
				console.debug("_scope", self._scope);
//				self._scope.$parent.$parent.loadUploads();
				modal.dismiss();
				$location.path(_getImportedDatasetPath());
			});
		};
		
		this.uid="4092";
	};
	return GeodsSearchViewModel;
});