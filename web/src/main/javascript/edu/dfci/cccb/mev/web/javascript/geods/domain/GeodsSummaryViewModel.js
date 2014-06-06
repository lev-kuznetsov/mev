define([],function(){
	var GeodsSummaryViewModel = function(MevGeodsSummaryFactory, scope, $modal, $location){
		//private
		var _self=this;
		
		function _showPleaseWait(){
			var modalInstance = $modal.open(
					{
						template: '<div class="mev-modal"><p> Please wait while, downloading GEO dataset ... </p><div>',
						size: 'lg'
					});
			return modalInstance;
		};		
		
		
		//public
		
		this.scope=scope;
		this.results=null;		
		
		this.searchByKeyword=function(keywords){
			_self.results=MevGeodsSummaryFactory.search(keywords);
		};
		this.import=function(summary){
			var modal = _showPleaseWait();
			summary.put(function(data){				
//				self.scope.$parent.$parent.loadUploads();
				modal.dismiss();
				$location.path(summary.getImportedDatasetPath());
			});
		};
		
		this.uid="";
		this.keywords="";
	};
	return GeodsSummaryViewModel;
});