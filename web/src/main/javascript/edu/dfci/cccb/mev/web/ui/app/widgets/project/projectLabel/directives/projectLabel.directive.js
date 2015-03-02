define([], function(){
	return function ProjectLabel(){
		return {
			scope: {
				project: '=',
				clickable: '@',
				editable: '@'
			},			
			restrict: "AE",
			templateUrl: "app/widgets/project/projectLabel/templates/projectLabel.tpl.html",			
			controller: "ProjectLabelVM",
			controllerAs: "ProjectLabelVM"
		}
	}
});