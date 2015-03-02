define(["ng"], function(ng){
	return function HomeVM (ProjectRepository, DatasourceRepository, Navigator){
		var self=this;
		this.projectName="aaa";
		this.selectedProject={name: "[none]"};
		this.createProject=function(){
			console.debug("in createProject");
			ProjectRepository.put({name: self.projectName});
		};
		this.showProject=function(){
			ProjectRepository.get(this.projectName).then(function(data){
				console.debug("showProject before", self.selectedProject, data);
				self.selectedProject=data;
				console.debug("showProject after", self.selectedProject, data);
			});
		};
		
		this.datasources=DatasourceRepository.getAll();
		this.importDataset=function(datasource, dataset){
			Navigator.importDataset(datasource, dataset);
		}
		this.toggle=function(){
			console.debug("COLLQ!")
			this.isCollapsed=!this.isCollapsed;
		}
		this.isCollapsed=false;
	}
});