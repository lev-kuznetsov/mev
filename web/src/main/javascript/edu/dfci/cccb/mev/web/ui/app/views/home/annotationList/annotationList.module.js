define(["ng", "./directives/annotationList.directive", "./controllers/annotationListCtrl", "app/domain/domain.module"], 
function(ng, AnnotationListDirective, AnnotationListCtrl){
	var module = ng.module("mui.annotationList", ["ui.router", "mui.annotations"]);
	module.directive("annotationList", AnnotationListDirective);
	module.controller("AnnotationListCtrl", ["$scope", "$state"
	     , "AnnotationSetRepository", "DashboardRepository", "ProjectRepository", AnnotationListCtrl]);
	return module;
});