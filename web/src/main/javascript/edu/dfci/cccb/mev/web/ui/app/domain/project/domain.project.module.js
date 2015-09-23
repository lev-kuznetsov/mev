define(["ng", "./model/MevProject"], 
function(ng, MevProject){
	var module = ng.module("mui.domain.project", ["mui.domain.dataset"]);
	module.factory("MevProject", MevProject);
	return module;
});