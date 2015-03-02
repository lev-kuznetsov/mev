define([], function(){
	return function(ProjectRepository, Navigator, $modal, $scope){
		var self=this;
		var projects=[];
		
		ProjectRepository.getAll().$promise.then(function(response){
			projects=response;
		});
		
		this.getProjectList=function(){
			return projects;
		};
		
		this.openProject=function(project){
			//navigate to project
			Navigator.openProject(project);
		};
		
		this.newProject=function(){
			Navigator.openProject({});
		};
		
		this.updateProject=function(project, newName){
			project.name=newName;
			console.log("updating project .. ",project);
			project.$update();
			
//			ProjectRepository.put(project).$promise.then(function(data){
//			console.log("updated project",data);
//			});
		};
		
	};	
});