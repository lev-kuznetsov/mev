define(["ng", "../controllers/annotationListCtrl"], function(ng){
	var AnnotationListDirective = 
		["AnnotationSetRepository",
		 "DashboardRepository",
		 function(AnnotationSetRepository, DashboardRepository){
			
		return {
			scope: {},
			restrict: "AE",
//			template:"<div>Annotation List</div>",
			templateUrl:"app/views/home/annotationList/directives/annotationListTablePlainJson.tpl.html",
			controller: "AnnotationListCtrl",
			link: function(scope, attr, elems, ctrl){
				
			}
		};
	}];
	return AnnotationListDirective;
});