define([], function(){
	return function(DatasetRepository, Navigator, $modal, $scope){
		var self=this;
		var datasets=DatasetRepository.getAll();
		
//		DatasetRepository.getAll().$promise.then(function(response){
//			datasets=response;
//		});
		
		this.getDatasetList=function(){
//			console.debug("getDatasetList: ", datasets);
			return datasets;
		};
		
		this.openDataset=function(dataset){
			//navigate to dataset
			Navigator.openDataset(dataset);
		};
		
		this.newDataset=function(){
			var dataset={				
				annotations: []
			};
			
			var modal=$modal.open({
				template: "<div>" +
				"<div class='modal-header'>"+
					"<h3 class='modal-title'>New Dataset</h3>"+
				"</div>"+
				"<div class='modal-body'>" +
						"<input type='text' ng-model='RenameDatasetCtrl.newName'></div>" +
				"</div>" +
				"<div class='modal-footer'>"+
					"<a ng-click='RenameDatasetCtrl.ok()'>OK</a> <a ng-click='RenameDatasetCtrl.cancel()'>Cancel</a>"+
            	"</div>"+
				"</div>",
				windowClass: "modal-vertical-centered",
				resolve: {
					dataset: function () {
						return dataset;
					}
			    },
			    controllerAs: "RenameDatasetCtrl",
				controller: function($scope, $modalInstance, dataset){
					this.newName=dataset.name;
					this.dataset=dataset;
					this.ok=function(){
						$modalInstance.close(this.newName);
					}
					this.cancel=function(){
						$modalInstance.dismiss("cancel");
					}
				}
			});
			
			modal.result.then(function(msg){
				dataset.name=msg;										
				DatasetRepository.put(dataset);
			}, function(msg){
				//cancel - do nothing
			});
						
//			Navigator.openDataset(newDataset);
		};
		
		this.updateDataset=function(dataset, newName){
			dataset.name=newName;
			console.log("updating dataset .. ",dataset);
			dataset.$update().then(function(){
				datasets=DatasetRepository.getAll();
			})
			
//			DatasetRepository.put(dataset).$promise.then(function(data){
//			console.log("updated dataset",data);
//			});
		};
		
	};	
});