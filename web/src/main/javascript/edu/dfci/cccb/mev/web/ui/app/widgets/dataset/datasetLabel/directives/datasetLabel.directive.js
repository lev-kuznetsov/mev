define([], function(){
	return function DatasetLabel(){
		return {
			scope: {
				dataset: '=',
				clickable: '@',
				editable: '@'
			},			
			restrict: "AE",
			templateUrl: "app/widgets/dataset/datasetLabel/templates/datasetLabel.tpl.html",			
			controller: "DatasetLabelVM",
			controllerAs: "DatasetLabelVM"
		}
	}
});