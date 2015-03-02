define([], function(){
	return function ProjectLabelVM(Navigator, $modal, $scope, $elem, $attr){
		this.openProject=function(project){
			//navigate to project			
			Navigator.openProject(project);
		};
		this.editListItem=function(project){			
			var modal=$modal.open({
				template: "<div>" +
				"<div class='modal-header'>"+
					"<h3 class='modal-title'>Rename</h3>"+
				"</div>"+
				"<div class='modal-body'>" +
						"<input type='text' ng-model='RenameProjectCtrl.newName'></div>" +
				"</div>" +
				"<div class='modal-footer'>"+
					"<a ng-click='RenameProjectCtrl.ok()'>OK</a> <a ng-click='RenameProjectCtrl.cancel()'>Cancel</a>"+
            	"</div>"+
				"</div>",
				windowClass: "modal-vertical-centered",
				resolve: {
					project: function () {
						return project;
					}
			    },
			    controllerAs: "RenameProjectCtrl",
				controller: function($scope, $modalInstance, project){
					this.newName=project.name;
					this.project=project;
					this.ok=function(){
						$modalInstance.close(this.newName);
					}
					this.cancel=function(){
						$modalInstance.dismiss("cancel");
					}
				}
			});
			
			modal.result.then(function(msg){
				project.name=msg;				
				project.$update();				
				
			}, function(msg){
				//cancel - do nothing
			})
		};	
	}
});