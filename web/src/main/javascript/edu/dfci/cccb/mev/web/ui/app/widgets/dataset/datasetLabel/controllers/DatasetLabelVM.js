define([], function(){
	var DatasetLabelVM=function DatasetLabelVM(ProjectRepository, Navigator, $modal, $scope, $elem, $attr){
		this.openDataset=function(dataset){
			//navigate to dataset			
			Navigator.openDataset(dataset);
		};
		this.editListItem=function(dataset){			
			var modal=$modal.open({
				template: "<div>" +
				"<div class='modal-header'>"+
					"<h3 class='modal-title'>Rename</h3>"+
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
				dataset.$update();				
				
			}, function(msg){
				//cancel - do nothing
			})
		};
		this.startNewProject=function(dataset){
			ProjectRepository.put({
				name: dataset.name,
				dataset: [dataset]
			}).$promise.then(function(response){
				Navigator.openProject(response);
			});
			
		}
		this.filterAnnotations=function(dataset, dimension){
			Navigator.openAnnotations(dataset, dimension);
		}
		this.addAnnotations=function(dataset, dimension){
			dataset.annotations = dataset.annotations || [];
			dataset.annotations[dimension]={
					id: dimension,
					name: dataset.name + " " + dimension + " annotatinos",					
					data: []
				};
			dataset.$update();
			Navigator.openAnnotations(dataset, dimension);
		}
	}
	DatasetLabelVM.$inject=["ProjectRepository", "Navigator","$modal", "$scope", "$element", "$attrs"];
	return DatasetLabelVM; 
});