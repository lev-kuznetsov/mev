define(["ng", "lodash"], function(ng, _){
	var ImportVM = function ImportVM(DatasetRepository, dataset, $state){
		var self=this;
		this.dataset = dataset;
		console.debug("IMPORTING >>", dataset)
		this.completeImport=function(){
			var importedDataset = _.cloneDeep(dataset);
			importedDataset.id=undefined;
			DatasetRepository.put(importedDataset);
			$state.go("root.home");
		};
	};
	
	ImportVM.$inject=["DatasetRepository", "dataset", "$state"]
	return ImportVM;
});