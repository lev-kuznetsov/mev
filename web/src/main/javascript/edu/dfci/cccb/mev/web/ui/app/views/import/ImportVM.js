define(["ng", "lodash"], function(ng, _){
	var ImportVM = function ImportVM(DatasetRepository, dataset, Navigator){
		var self=this;
		this.dataset = dataset;
		console.debug("IMPORTING >>", dataset)
		this.completeImport=function(){
			var importedDataset = _.cloneDeep(dataset);
			importedDataset.id=undefined;
			DatasetRepository.put(importedDataset);
			Navigator.goHome();
		};
	};
	
	ImportVM.$inject=["DatasetRepository", "dataset", "Navigator"]
	return ImportVM;
});