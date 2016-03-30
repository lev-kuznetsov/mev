define(["lodash"], function(_){
	var MockDirective = function MockDirective(mevMockVM){
		return {
			restrict: "EAC",
			template: "<div><div>mock xyz: {{vm.getText()}}</div><div>{{vm.getDatasetName()}}</div></div>",
			link: function(scope, elm, attr){
				_.extend(scope.vm || (scope.vm={}), mevMockVM);
			}
		};	
	};

	MockDirective.$inject=["mevMockVM"];
	MockDirective.$name="mevMockDirective";	
	return MockDirective;
});