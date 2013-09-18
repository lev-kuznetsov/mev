drct.directive('tupleSelector', ['$compile', function(compile) {
	return {
		restrict: 'E',
		scope: {
			inputfield: "=",
			pushToQuery: "&"
		},
		template: "<div> Blah </div>",
	}
}]);