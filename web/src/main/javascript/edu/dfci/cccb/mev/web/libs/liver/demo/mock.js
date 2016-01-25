define([], function(){
	var MockDirective = function MockDirective(){
		return {
			restrict: "EAC",
			template: "<div>mock</div>"
		}
	};

	MockDirective.$inject=[];
	MockDirective.$name="mevMockDirective";	
	return MockDirective;
});