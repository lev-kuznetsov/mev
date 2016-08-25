define(["ng", "./dashboard/domain.dataset.dashboard.module"], 
function(ng, project){
	var module = ng.module("mui.domain.dataset", arguments, arguments);
	var DatasetFactory = function DatasetFactory(DS){
			return DS.defineResource({
				name: 'Dataset',
				endpoint: "dataset",
				idAttribute: "name",
				relations: {
					belongsTo: {
						Project: {
							localField: "project",
							localKey: "projectName",
							parent: true,
							enumerable: true
						}
					}
				}
			});
	};
	DatasetFactory.$inject=["DS"];
	module.factory("MevDataset", DatasetFactory);
	return module;
});