define(["ng"], function(ng){
	"use strict";
	var ProjectFactory = function ProjectFactory(DS){		
		return DS.defineResource({
			name: 'Project',
			endpoint: "project",
			idAttribute: "name",
			relations: {
				hasMany: {
					Dataset: {
						localField: "datasets",
						foreignKey: "projectName",
						enumerable: true
					}
				}
			}
		});		
	};
	ProjectFactory.$inject = ["DS"];
	return ProjectFactory;
});