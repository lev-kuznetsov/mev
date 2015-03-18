define(["ng"], function(ng){
	var PanelControllsDirective=function($compile){
		return {
			restrict: "EA",			
			controller: ["$scope", function($scope){
				$scope.panelControlls={
					isCollapsed: false,
					toggle: function(){
						console.debug("panelControlls.isCollapsed!");
						$scope.panelControlls.isCollapsed=!$scope.panelControlls.isCollapsed;
					}
				};
			}],
			compile: function(tElem, tAttr){
				var notHeading = tElem.children(":not(.panel-heading)");
				notHeading.attr("collapse", "panelControlls.isCollapsed");
				
				return {
					post: function(){},
					pre: function(scope, elem, attr, controller){
						
						var heading = elem.children(".panel-heading");
						
						heading.prepend($compile("<div class='panel-controls btn-group pull-right' >" +
								"<button type=\"button\" class=\"btn\" ng-click=\"panelControlls.toggle()\">\n" + 
								"		        <i ng-class=\"{\'fa fa-caret-up\': !panelControlls.isCollapsed, \'fa fa-caret-down\': panelControlls.isCollapsed}\"></i>\n" + 
								"		      </button>		    \n" + 
								"</div>")(scope));
						
						elem.on("click", function(){
							console.debug("CLIKKC PANEL");
						});
					}
				};
			}
		};
	};
	PanelControllsDirective.$inject=["$compile"];
	return PanelControllsDirective;
});