define(["ng", 
        "./annotations/annotations.module",
        "./dashboard/dashboard.module",
        "./facet/facet.module",
        "./project/project.domain.module",
        "./datasource/datasource.module",
        "./dataset/dataset.module",
        "./navigator/domain.navigator.module"], 
function(ng, annotationsMod, dashboardMod, facetMod, projectMod, datasourceMod, datasetMod, navigatorMod){
	var module = ng.module("mui.domain", [annotationsMod.name, 
	                                      dashboardMod.name,
	                                      facetMod.name, 
	                                      projectMod.name,
	                                      datasourceMod.name,
	                                      datasetMod.name,
	                                      navigatorMod.name]);
	return module;
});