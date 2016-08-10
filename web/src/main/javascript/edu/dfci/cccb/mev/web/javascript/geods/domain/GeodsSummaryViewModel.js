define(["lodash"],function(_){
	var GeodsSummaryViewModel = function(MevGeodsSummaryFactory, scope, $modal, $location){
		//private
		var _self=this;
		
		function _showPleaseWait(){
			var modalInstance = $modal.open(
					{
						template: '<div class="mev-modal"><p> Please wait, downloading GEO dataset ... </p><div>',
						size: 'lg'
					});
			return modalInstance;
		};		
		
		
		//public
		
		this.scope=scope;
		this.results=null;
		this.itemsPerPage = 20;
		this.pagination = {
			current: 1
		};
		this.searchByKeyword=function(keywords){
			_self.results=MevGeodsSummaryFactory.search(keywords, 0, _self.itemsPerPage);
		};
		this.import=function(summary){
			var modal = _showPleaseWait();
			summary.put(function(data){				
//				self.scope.$parent.$parent.loadUploads();
				modal.dismiss();
				$location.path(summary.getImportedDatasetPath());
			});
		};
		this.recordCount = function(){
			return _.get(_self, "results.results.uids.esearchresult.count");
		};

		this.pageChanged = function(newPageNumber){
			_self.results=MevGeodsSummaryFactory.search(_self.keywords, newPageNumber-1, _self.itemsPerPage);
		};
		this.firstRecordNumber=function(){
			return (_self.pagination.current-1)*_self.itemsPerPage+1;
		};
		this.lastRecordNumber=function(){
			return _self.firstRecordNumber()+_self.itemsPerPage-1;
		};
		this.uid="";
		this.keywords="";
	};
	return GeodsSummaryViewModel;
});